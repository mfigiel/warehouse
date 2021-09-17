package metrics.service;

public enum Metrics {
        ADDED_PRODUCT("productAdded"),
        DOWNLOADED_PRODUCTS("productsDownloaded"),
        PRODUCT_INFORMATIONS_GET("productsInformationGet"),
        BOUGHT_PRODUCT("productBought");

        private String stringValue;

        Metrics(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }
}
