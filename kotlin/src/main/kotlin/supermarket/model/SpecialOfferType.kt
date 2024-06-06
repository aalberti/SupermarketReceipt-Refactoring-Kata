package supermarket.model

enum class SpecialOfferType {
    ThreeForTwo {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() > 2

        override fun discount(
            offerArgument: Double,
            productQuantity: ProductQuantity,
            unitPrice: Double
        ): Discount {
            val numberOfDiscounts = productQuantity.quantity.toInt() / 3
            val discountAmount =
                productQuantity.quantity * unitPrice - (numberOfDiscounts.toDouble() * 2.0 * unitPrice + productQuantity.quantity % 3 * unitPrice)
            return Discount(productQuantity.product, "3 for 2", discountAmount)
        }
    },
    TenPercentDiscount {
        override fun applies(productQuantity: ProductQuantity): Boolean = true

        override fun discount(
            offerArgument: Double,
            productQuantity: ProductQuantity,
            unitPrice: Double
        ): Discount = Discount(
            productQuantity.product, "$offerArgument% off",
            productQuantity.quantity * unitPrice * offerArgument / 100.0
        )
    },
    TwoForAmount {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() >= 2

        override fun discount(
            offerArgument: Double,
            productQuantity: ProductQuantity,
            unitPrice: Double
        ): Discount {
            val total =
                offerArgument * (productQuantity.quantity.toInt() / 2) + productQuantity.quantity % 2 * unitPrice
            val amount = unitPrice * productQuantity.quantity - total
            return Discount(productQuantity.product, "2 for $offerArgument", amount)
        }
    },
    FiveForAmount {
        override fun applies(productQuantity: ProductQuantity): Boolean =
            productQuantity.quantity.toInt() >= 5

        override fun discount(
            offerArgument: Double,
            productQuantity: ProductQuantity,
            unitPrice: Double
        ): Discount {
            val numberOfDiscounts = productQuantity.quantity.toInt() / 5
            val discountTotal =
                unitPrice * productQuantity.quantity - (offerArgument * numberOfDiscounts + productQuantity.quantity % 5 * unitPrice)
            return Discount(productQuantity.product, "5 for $offerArgument", discountTotal)
        }
    };

    abstract fun applies(productQuantity: ProductQuantity): Boolean

    abstract fun discount(
        offerArgument: Double,
        productQuantity: ProductQuantity,
        unitPrice: Double
    ): Discount
}
