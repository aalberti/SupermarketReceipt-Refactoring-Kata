package supermarket.model

class Offer(private var offerType: SpecialOfferType, internal val product: Product, private var argument: Double) {
    fun discount(
        catalog: SupermarketCatalog,
        productQuantity: ProductQuantity
    ): Discount? =
        if (offerType.applies(productQuantity))
            offerType.discount(argument, productQuantity, catalog.getUnitPrice(productQuantity.product))
        else null
}
