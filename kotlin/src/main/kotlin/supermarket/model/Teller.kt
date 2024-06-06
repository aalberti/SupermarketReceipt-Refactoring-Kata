package supermarket.model

class Teller(private val catalog: SupermarketCatalog) {
    private val offers = ArrayList<Offer>()

    fun addSpecialOffer(offer: Offer) {
        if (offer is ByProductOffer)
            offers.firstOrNull { it is ByProductOffer && it.product == offer.product }
                ?.let { offers.remove(it) }
        offers.add(offer)
    }

    fun checksOutArticlesFrom(cart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = cart.items
        for (pq in productQuantities) {
            val p = pq.product
            val quantity = pq.quantity
            val unitPrice = catalog.getUnitPrice(p)
            val price = quantity * unitPrice
            receipt.addProduct(p, quantity, unitPrice, price)
        }
//==== NEXT: Introduce bundle offers
        for (offer in offers)
            offer.discount(cart, catalog)?.let { discount -> receipt.addDiscount(discount) }

        return receipt
    }
}
