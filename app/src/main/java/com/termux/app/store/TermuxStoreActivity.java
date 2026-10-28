package com.termux.app.store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.termux.R;
import com.termux.app.TermuxActivity;
import com.termux.app.TermuxService;
import com.termux.shared.activity.media.AppCompatActivityUtils;
import com.termux.shared.net.uri.UriUtils;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_SERVICE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TermuxStoreActivity extends AppCompatActivity implements StorePackageAdapter.OnPackageActionListener {

    private RecyclerView mRecyclerView;
    private StorePackageAdapter mAdapter;
    private List<StorePackageItem> mPackages;

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, TermuxStoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        AppCompatActivityUtils.setToolbar(this, com.termux.shared.R.id.toolbar);
        AppCompatActivityUtils.setShowBackButtonInActionBar(this, true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Play Store");
            getSupportActionBar().setSubtitle("Gestor de paquetes de Termux");
        }

        mRecyclerView = findViewById(R.id.store_packages_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        initPackageList();
        mAdapter = new StorePackageAdapter(mPackages, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initPackageList() {
        mPackages = new ArrayList<>();

        // 1. Yazi File Manager
        mPackages.add(new StorePackageItem(
            "yazi",
            "Yazi",
            "Productividad • Terminal File Manager",
            "Navegador de archivos ultrarrápido escrito en Rust con soporte para vista previa gráfica y asíncrona.",
            R.drawable.ic_store_yazi,
            "pkg install yazi -y",
            "pkg uninstall yazi -y",
            "pkg install yazi -y",
            TermuxConstants.TERMUX_BIN_PREFIX_DIR_PATH + "/yazi"
        ));

        // 2. Node.js LTS
        mPackages.add(new StorePackageItem(
            "nodejs-lts",
            "Node.js LTS",
            "Desarrollo • Javascript Runtime",
            "Entorno de ejecución para JavaScript en el servidor (Long Term Support) con gestor de paquetes npm.",
            R.drawable.ic_store_antigravity,
            "pkg install nodejs-lts -y",
            "pkg uninstall nodejs-lts -y",
            "pkg install nodejs-lts -y",
            TermuxConstants.TERMUX_BIN_PREFIX_DIR_PATH + "/node"
        ));

        // 3. Antigravity CLI
        mPackages.add(new StorePackageItem(
            "antigravity-cli",
            "Antigravity CLI",
            "Herramientas IA • Asistente de Código",
            "Agente inteligente para desarrollo de software avanzado, pairing y automatización completa.",
            R.drawable.ic_store_antigravity,
            "curl -fsSL https://antigravity.google/cli/install.sh | bash",
            "rm -rf ~/.gemini/antigravity-cli ~/.local/bin/agy",
            "curl -fsSL https://antigravity.google/cli/install.sh | bash",
            "/data/data/com.termux/files/home/.local/bin/agy"
        ));

        // 4. Termux Storage Setup
        mPackages.add(new StorePackageItem(
            "termux-setup-storage",
            "Termux Storage",
            "Sistema • Permisos de Almacenamiento",
            "Configura el acceso al almacenamiento compartido de Android (~/storage/shared, descargas y DCIM).",
            R.drawable.ic_store_antigravity,
            "termux-setup-storage",
            null,
            null,
            null
        ));

        // 5. Backup Termux
        mPackages.add(new StorePackageItem(
            "termux-backup",
            "Termux Backup",
            "Utilidades • Copia de Seguridad",
            "Crea una copia de seguridad comprimida completa de home y usr en /sdcard/Download/termux/termux-backup.tar.gz.",
            R.drawable.ic_store_antigravity,
            "mkdir -p /sdcard/Download/termux && cd /data/data/com.termux/files && tar -zcvf /sdcard/Download/termux/termux-backup.tar.gz home usr",
            null,
            null,
            null
        ));

        // 6. Restore Termux Backup
        mPackages.add(new StorePackageItem(
            "termux-restore",
            "Termux Restore",
            "Utilidades • Restaurar Copia",
            "Restaura la copia de seguridad previa desde /sdcard/Download/termux/termux-backup.tar.gz.",
            R.drawable.ic_store_antigravity,
            "cd /data/data/com.termux/files && tar -zxvf /sdcard/Download/termux/termux-backup.tar.gz --recursive-unlink",
            null,
            null,
            null
        ));
    }

    @Override
    public void onActionClick(StorePackageItem item, boolean isInstalled) {
        String commandToRun;
        if (isInstalled && item.getUpdateCommand() != null) {
            commandToRun = item.getUpdateCommand();
        } else {
            commandToRun = item.getInstallCommand();
        }
        executeShellCommandInTerminal(item.getTitle(), commandToRun);
    }

    @Override
    public void onSecondaryActionClick(StorePackageItem item) {
        if (item.getUninstallCommand() != null) {
            executeShellCommandInTerminal(item.getTitle() + " (Desinstalar)", item.getUninstallCommand());
        }
    }

    private void executeShellCommandInTerminal(String title, String shellCommand) {
        try {
            // Write wrapper script in files cache dir
            File scriptDir = new File(getCacheDir(), "store_scripts");
            if (!scriptDir.exists()) scriptDir.mkdirs();
            File scriptFile = new File(scriptDir, "run_store_action.sh");

            String scriptContent = "#!/data/data/com.termux/files/usr/bin/bash\n" +
                "echo -e '\\033[1;32m==> Termux Play Store: " + title + "\\033[0m'\n" +
                "echo -e '\\033[1;34m$ " + shellCommand.replace("'", "'\\''") + "\\033[0m'\n" +
                "echo '----------------------------------------'\n" +
                shellCommand + "\n" +
                "EXIT_CODE=$?\n" +
                "echo '----------------------------------------'\n" +
                "if [ $EXIT_CODE -eq 0 ]; then\n" +
                "  echo -e '\\033[1;32m✓ Proceso finalizado con éxito (código 0)\\033[0m'\n" +
                "else\n" +
                "  echo -e '\\033[1;31m✗ Error al ejecutar (código '$EXIT_CODE')\\033[0m'\n" +
                "fi\n";

            try (FileOutputStream fos = new FileOutputStream(scriptFile)) {
                fos.write(scriptContent.getBytes());
            }
            scriptFile.setExecutable(true, false);

            final Uri scriptUri = UriUtils.getFileUri(scriptFile.getAbsolutePath());
            Intent executeIntent = new Intent(TERMUX_SERVICE.ACTION_SERVICE_EXECUTE, scriptUri);
            executeIntent.setClass(this, TermuxService.class);
            executeIntent.putExtra(TERMUX_SERVICE.EXTRA_SHELL_NAME, title);
            startService(executeIntent);

            // Open or bring TermuxActivity to the front to watch live logs
            Intent termuxIntent = new Intent(this, TermuxActivity.class);
            termuxIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(termuxIntent);

            Toast.makeText(this, "Ejecutando en la terminal: " + title, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error preparando script: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
