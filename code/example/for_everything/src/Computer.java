public class Computer {

    // required parameters
    private String hdd;
    private String ram;

    private int g;
    // optional parameters
    private String isGraphicsCardEnabled;
    private boolean isBluetoothEnabled;

    public String getHdd() {
        return hdd;
    }

    public String getRam() {
        return ram;
    }

    public int getG() {
        return g;
    }

    public String isGraphicsCardEnabled() {
        return isGraphicsCardEnabled;
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    public static ComputerBuilder builder(String hdd, String ram) {
        return new ComputerBuilder(hdd, ram);
    }

    private Computer(ComputerBuilder builder) {
        this.hdd = builder.hdd;
        this.ram = builder.ram;
        this.isGraphicsCardEnabled = builder.isGraphicsCardEnabled;
        this.isBluetoothEnabled = builder.isBluetoothEnabled;
    }

    // Builder Class
    public static class ComputerBuilder {

        // required parameters
        private String hdd;
        private String ram;

        private int g;
        // optional parameters
        private String isGraphicsCardEnabled;
        private boolean isBluetoothEnabled;

        private ComputerBuilder(String hdd, String ram) {
            this.hdd = hdd;
            this.ram = ram;
        }

        public ComputerBuilder setGraphicsCardEnabled(String isGraphicsCardEnabled) {
            this.isGraphicsCardEnabled = isGraphicsCardEnabled;
            return this;
        }

        public ComputerBuilder setBluetoothEnabled(boolean isBluetoothEnabled) {
            this.isBluetoothEnabled = isBluetoothEnabled;
            return this;
        }

        public ComputerBuilder setBluetoothEnabled(int isBluetoothEnabled) {
            this.g = isBluetoothEnabled;
            return this;
        }
        public Computer build() {
            return new Computer(this);
        }

    }

}