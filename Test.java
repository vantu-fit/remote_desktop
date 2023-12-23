public class Test {
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);

        if (os.contains("linux")) {
            // Kiểm tra nếu là Kali Linux
            if (isKaliLinux()) {
                System.out.println("Kali Linux");
            } else {
                System.out.println("Linux (other distribution)");
            }
        } else if (os.contains("mac")) {
            System.out.println("Mac OS");
        } else if (os.contains("windows")) {
            System.out.println("Windows");
        } else {
            System.out.println("Other");
        }
    }

    private static boolean isKaliLinux() {
        String version = System.getProperty("os.version").toLowerCase();
        String name = System.getProperty("os.name").toLowerCase();

        // Kiểm tra phiên bản và tên để xác định Kali Linux
        return name.contains("linux") && version.contains("kali");
    }
}
