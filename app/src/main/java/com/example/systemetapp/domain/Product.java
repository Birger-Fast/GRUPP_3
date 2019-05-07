package com.example.systemetapp.domain;

public class Product {

    private String name;
    private double price; // SEK
    private double alcohol; // % alcohol by volume
    private int volume; // milliliters
    private int nr; // XML: <nr>nnn</nr> unique nr in the catalog
    private String productGroup; // e.g. <Varugrupp>Okryddad sprit</Varugrupp>
    private String type; // e.g. <Typ>Syrlig öl</Typ>

    /**
     * Defines a Builder for an Object.
     */
    public static class Builder {
        private String name;
        private double price; // SEK
        private double alcohol; // % alcohol by volume
        private int volume; // milliliters
        private int nr; // XML: <nr>nnn</nr> unique nr in the catalog
        private String productGroup; // e.g. <Varugrupp>Okryddad sprit</Varugrupp>
        private String type;

        /**
         * Provides the Product name
         * @param name The name for the Product to build
         * @return this Builder so that you can continue building
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         *
         */
        public Builder price(double price) {
            this.price = price;
            return this;
        }

        /**
         *
         */
        public Builder alcohol(double alcohol) {
            this.alcohol = alcohol;
            return this;
        }

        /**
         *
         */
        public Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        /**
         *
         */
        public Builder nr(int nr) {
            this.nr = nr;
            return this;
        }

        /**
         *
         */
        public Builder productGroup(String productGroup) {
            this.productGroup = productGroup;
            return this;
        }

        /**
         *
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         *
         */
        public Product build() {
            return new Product(this);
        }
    }

    /**
     * Constructs a Product using a Product.Builder
     *
     * @param builder The builder to use for building this Product
     */
    public Product(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.alcohol = builder.alcohol;
        this.volume = builder.volume;
        this.nr = builder.nr;
        this.productGroup = builder.productGroup;
        this.type = builder.type;
    }

    /**
     * Constructs a new Product.
     * @param name The name of this Product
     * @param alcohol The alcohol level (in percent alcohol by weight) of this Product
     * @param price The price of this Product (in SEK)
     * @param volume The volume (in millilitres) of this product
     */
    public Product(String name, double alcohol, double price, int volume) {
        this.name = name;
        this.alcohol = alcohol;
        this.price = price;
        this.volume = volume;
    }

    /**
     * Returns the name of this Product
     * @return The name of this Product
     */
    public String name() { return name; }

    /**
     * Returns the alcohol level of this Product
     * @return The alcohol level of this Product
     */
    public double alcohol() { return alcohol; }

    /**
     * Returns the price of this Product
     * @return The price of this Product
     */
    public double price() { return price; }

    /**
     * Returns the volume of this Product
     * @return The volume of this Product
     */
    public int volume() { return volume; }

    /**
     * Returns the product group of this Product
     * @return The product group of this Product
     */
    public String productGroup() {
        return productGroup;
    }

    /**
     * Returns the product type of this Product
     * @return The product type of this Product
     */
    public String type() {
        return type;
    }

    /**
     * Returns the product number of this Product
     * @return The product number of this Product
     */
    public int nr() {
        return nr;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Uses name, alcohol, volume, price and nr
     * when deciding if this Product is equal to
     * another Product.
     *
     * Note: We could have settled with nr, since
     * it is the identity of a Product from the Systembolaget
     * source data (the XML file etc).
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (! (other instanceof Product)) {
            return false;
        }
        Product that = (Product)other;
        return this.name.equals(that.name) &&
                this.alcohol == that.alcohol &&
                this.volume == that.volume &&
                this.price == that.price &&
                this.nr == that.nr;
    }

    @Override
    public int hashCode() {
        // Using hash algorithm from
        // Joshua Bloch - Effective Java - on hashcodes
        int code = 17;
        code = 31 * code + name.hashCode();
        long alc = Double.doubleToLongBits(alcohol);
        code = 31 * code + (int) (alc ^ (alc >>> 32));
        code = 31 * code + volume;
        long pri = Double.doubleToLongBits(price);
        code = 31 * code + (int) (pri ^ (pri >>> 32));
        code = 31 * code + nr;
        return code;
    }
}