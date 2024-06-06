package supermarket.model

class ShoppingCart {
    private val items = ArrayList<ProductQuantity>()

    internal fun getItems(): List<ProductQuantity> {
        return ArrayList(items)
    }

    fun addItem(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
    }

    internal fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SupermarketCatalog) {
        for (item in items) {
            offers[item.product]?.let { offer ->
                offer.discount(catalog, item)?.let { discount -> receipt.addDiscount(discount) }
            }
        }
    }
}
