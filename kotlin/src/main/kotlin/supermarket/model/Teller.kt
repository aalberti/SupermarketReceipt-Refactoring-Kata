package supermarket.model

import java.util.HashMap

class Teller(private val catalog: SupermarketCatalog) {
    private val offers = HashMap<Product, Offer>()

    fun addSpecialOffer(offer: Offer) {
        this.offers[offer.product] = offer
    }

    fun checksOutArticlesFrom(theCart: ShoppingCart): Receipt {
        val receipt = Receipt()
        val productQuantities = theCart.getItems()
        for (pq in productQuantities) {
            val p = pq.product
            val quantity = pq.quantity
            val unitPrice = this.catalog.getUnitPrice(p)
            val price = quantity * unitPrice
            receipt.addProduct(p, quantity, unitPrice, price)
        }
        theCart.handleOffers(receipt, this.offers, this.catalog)

        return receipt
    }
}
