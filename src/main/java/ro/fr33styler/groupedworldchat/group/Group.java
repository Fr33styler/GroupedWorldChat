package ro.fr33styler.groupedworldchat.group;

public class Group {

    private final String id;
    private final String name;
    private final String format;

    public Group(String id, String name, String format) {
        this.id = id;
        this.name = name.replace('&', '§');
        this.format = format.replace('&', '§').replace("%player%", "%1$s").replace("%message%", "%2$s");
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

}