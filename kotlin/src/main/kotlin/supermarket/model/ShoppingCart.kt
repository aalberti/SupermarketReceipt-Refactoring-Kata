package supermarket.model

class ShoppingCart {
    private val items = ArrayList<ProductQuantity>()

    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        for (item in items) {
            val product = item.product
            val quantity = item.quantity
            if (offers.containsKey(product)) {
                val offer = offers[product]!!
                offer.discount(quantity, catalog, product)
                    ?.let { receipt.addDiscount(it) }
            }
        }
    }
}
