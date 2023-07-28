package bg.sofia.uni.fmi.mjt.news;

public class Parameters {
    // required parameters
    private String[] keyWords;

    // optional parameters
    private String category;
    private String country;
    private int pageSize;
    private int page;

    public String[] getKeyWords() {
        return keyWords;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public static ParametersBuilder builder(String [] arr) {
        return new ParametersBuilder(arr);
    }

    public Parameters(ParametersBuilder builder) {
        this.keyWords = builder.keyWords;
        this.category = builder.category;
        this.country = builder.country;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
    }

    // Builder Class
    public static class ParametersBuilder {

        // required parameters
        private String[] keyWords;

        // optional parameters
        private String category;
        private String country;
        private int pageSize;
        private int page;

        private ParametersBuilder(String[] keyWords) {
            this.keyWords = keyWords;
        }

        public ParametersBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public ParametersBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public ParametersBuilder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public ParametersBuilder setPage(int page) {
            this.page = page;
            return this;
        }

        public Parameters build() {
            return new Parameters(this);
        }

    }
}
