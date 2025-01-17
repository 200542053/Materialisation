package me.shedaniel.materialisation.config;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.List;

public class ConfigPackInfo {

    private String displayName;
    private String identifier;
    private List<String> requiredMods;
    private List<String> authors;
    private String version;
    private String description;
    private transient Identifier identifierObject;
    private transient Version versionObject;

    public ConfigPackInfo(String displayName, String identifier, List<String> requiredMods, List<String> authors, String version) {
        this.displayName = displayName;
        this.identifier = identifier;
        this.requiredMods = requiredMods;
        this.authors = authors;
        this.version = version;
    }

    public ConfigPackInfo withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Identifier getIdentifier() {
        if (identifierObject == null) {
            try {
                identifierObject = Identifier.tryParse(identifier);
                if (identifierObject == null)
                    throw new InvalidIdentifierException("");
                return identifierObject;
            } catch (InvalidIdentifierException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return identifierObject;
    }

    public List<String> getRequiredMods() {
        return requiredMods;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public Version getVersion() {
        if (versionObject == null) {
            try {
                return versionObject = Version.parse(version);
            } catch (VersionParsingException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return versionObject;
    }

}
