package supermarket

import supermarket.model.*
import java.util.*

class ReceiptPrinter @JvmOverloads constructor(private val columns: Int = 40) {
    fun printReceipt(receipt: Receipt): String = listOf(
        formatReceiptItems(receipt.getItems()),
        discountsDescription(receipt.getDiscounts()),
        formatTotal(receipt.totalPrice)
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")

    private fun formatReceiptItems(receiptItems: List<ReceiptItem>): String = receiptItems
        .joinToString(separator = "\n") { formatReceiptItem(it) }

    private fun formatReceiptItem(item: ReceiptItem): String {
        var line = padToLineSize(item.product.name, formatAmount(item.totalPrice))
        if (item.quantity != 1.0)
            line += "\n  ${formatAmount(item.price)} * ${formatQuantity(item)}"
        return line
    }

    private fun formatQuantity(item: ReceiptItem): String =
        if (ProductUnit.Each == item.product.unit)
            String.format("%x", item.quantity.toInt())
        else
            String.format(Locale.UK, "%.3f", item.quantity)

    private fun discountsDescription(discounts: List<Discount>) =
        if (discounts.isEmpty()) ""
        else discounts.joinToString("\n") { discountDescription(it) }

    private fun discountDescription(discount: Discount): String {
        val productPresentation = if (discount is ProductDiscount) "(${discount.product.name})" else ""
        return padToLineSize(
            "${discount.description}$productPresentation",
            "-${formatAmount(discount.discountAmount)}"
        )
    }

    private fun formatTotal(totalPrice: Double): String =
        "\n${padToLineSize("Total: ", formatAmount(totalPrice))}"

    private fun formatAmount(amount: Double) = String.format(Locale.UK, "%.2f", amount)

    private fun padToLineSize(left: String, right: String): String =
        "$left${whitespace(columns - left.length - right.length)}$right"

    private fun whitespace(whitespaceSize: Int): String {
        val whitespace = StringBuilder()
        for (i in 0 until whitespaceSize) {
            whitespace.append(" ")
        }
        return whitespace.toString()
    }
}
