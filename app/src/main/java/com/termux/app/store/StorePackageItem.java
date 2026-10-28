package com.termux.app.store;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StorePackageItem {

    public enum ActionType {
        INSTALL,
        UPDATE,
        UNINSTALL,
        EXECUTE
    }

    private final String id;
    private final String title;
    private final String category;
    private final String description;
    @DrawableRes
    private final int iconResId;
    private final String installCommand;
    @Nullable
    private final String uninstallCommand;
    @Nullable
    private final String updateCommand;
    @Nullable
    private final String binaryCheckPath;

    public StorePackageItem(@NonNull String id,
                            @NonNull String title,
                            @NonNull String category,
                            @NonNull String description,
                            @DrawableRes int iconResId,
                            @NonNull String installCommand,
                            @Nullable String uninstallCommand,
                            @Nullable String updateCommand,
                            @Nullable String binaryCheckPath) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.iconResId = iconResId;
        this.installCommand = installCommand;
        this.uninstallCommand = uninstallCommand;
        this.updateCommand = updateCommand;
        this.binaryCheckPath = binaryCheckPath;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getInstallCommand() {
        return installCommand;
    }

    @Nullable
    public String getUninstallCommand() {
        return uninstallCommand;
    }

    @Nullable
    public String getUpdateCommand() {
        return updateCommand;
    }

    @Nullable
    public String getBinaryCheckPath() {
        return binaryCheckPath;
    }
}
