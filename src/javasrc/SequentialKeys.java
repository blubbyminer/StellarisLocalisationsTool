package javasrc;

public enum SequentialKeys {
    O("ORD"),
    C("C"),
    CC("CC"),
    CCC("CCC"),
    R("R");

    final String label;
    SequentialKeys(String label) {
        this.label = label;
    }
}