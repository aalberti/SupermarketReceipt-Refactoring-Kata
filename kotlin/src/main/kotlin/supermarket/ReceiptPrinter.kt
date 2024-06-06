package supermarket

import supermarket.model.*
import java.util.*

class ReceiptPrinter @JvmOverloads constructor(private val columns: Int = 40) {

    fun printReceipt(receipt: Receipt): String {
        val result = StringBuilder()
        for (item in receipt.getItems()) {
            val price = String.format(Locale.UK, "%.2f", item.totalPrice)
            val quantity = presentQuantity(item)
            val name = item.product.name
            val unitPrice = String.format(Locale.UK, "%.2f", item.price)

            val whitespaceSize = columns - name.length - price.length
            var line = name + getWhitespace(whitespaceSize) + price + "\n"

            if (item.quantity != 1.0) {
                line += "  $unitPrice * $quantity\n"
            }
            result.append(line)
        }
        result.append(discountsDescription(receipt.getDiscounts()))
        result.append("\n")
        val pricePresentation = String.format(Locale.UK, "%.2f", receipt.totalPrice as Double)
        val total = "Total: "
        val whitespace = getWhitespace(columns - total.length - pricePresentation.length)
        result.append(total).append(whitespace).append(pricePresentation)
        return result.toString()
    }

    private fun discountsDescription(discounts: List<Discount>) =
        if (discounts.isEmpty()) ""
        else discounts.joinToString("\n") { discountDescription(it) } + "\n"

    private fun discountDescription(discount: Discount): String {
        val productPresentation = if (discount is ProductDiscount) "(${discount.product.name})" else ""
        val pricePresentation = String.format(Locale.UK, "%.2f", discount.discountAmount)
        val description = discount.description
        val whitespace =
            getWhitespace(columns - 1 - productPresentation.length - description.length - pricePresentation.length)
        return "$description$productPresentation$whitespace-$pricePresentation"
    }

    private fun presentQuantity(item: ReceiptItem): String {
        return if (ProductUnit.Each == item.product.unit)
            String.format("%x", item.quantity.toInt())
        else
            String.format(Locale.UK, "%.3f", item.quantity)
    }

    private fun getWhitespace(whitespaceSize: Int): String {
        val whitespace = StringBuilder()
        for (i in 0 until whitespaceSize) {
            whitespace.append(" ")
        }
        return whitespace.toString()
    }
}
