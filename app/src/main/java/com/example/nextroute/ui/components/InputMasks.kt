package com.example.nextroute.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private data class MaskResult(
    val text: String,
    val originalToTransformed: IntArray
)

private fun phoneMask(raw: String): MaskResult {
    val digits = raw.filter(Char::isDigit).take(11)
    val result = StringBuilder()
    val mapping = IntArray(digits.length + 1)

    mapping[0] = 0

    digits.forEachIndexed { index, digit ->
        if (index == 0) result.append('(')
        result.append(digit)
        mapping[index + 1] = result.length

        if (index == 1 && digits.length > 2) result.append(") ")
        if (index == 6 && digits.length > 7) result.append('-')
    }

    return MaskResult(result.toString(), mapping)
}

private fun cpfMask(raw: String): MaskResult {
    val digits = raw.filter(Char::isDigit).take(11)
    val result = StringBuilder()
    val mapping = IntArray(digits.length + 1)

    mapping[0] = 0

    digits.forEachIndexed { index, digit ->
        result.append(digit)
        mapping[index + 1] = result.length

        if (index == 2 && digits.length > 3) result.append('.')
        if (index == 5 && digits.length > 6) result.append('.')
        if (index == 8 && digits.length > 9) result.append('-')
    }

    return MaskResult(result.toString(), mapping)
}

private class SafeOffsetMapping(
    private val originalToTransformed: IntArray,
    private val transformedLength: Int
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        return originalToTransformed[offset.coerceIn(0, originalToTransformed.lastIndex)]
    }

    override fun transformedToOriginal(offset: Int): Int {
        val safeOffset = offset.coerceIn(0, transformedLength)

        var result = 0

        for (index in originalToTransformed.indices) {
            if (originalToTransformed[index] <= safeOffset) {
                result = index
            } else {
                break
            }
        }

        return result
    }
}

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val result = phoneMask(text.text)

        return TransformedText(
            text = AnnotatedString(result.text),
            offsetMapping = SafeOffsetMapping(
                originalToTransformed = result.originalToTransformed,
                transformedLength = result.text.length
            )
        )
    }
}

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val result = cpfMask(text.text)

        return TransformedText(
            text = AnnotatedString(result.text),
            offsetMapping = SafeOffsetMapping(
                originalToTransformed = result.originalToTransformed,
                transformedLength = result.text.length
            )
        )
    }
}

fun onlyDigits(value: String, maxLength: Int): String {
    return value.filter(Char::isDigit).take(maxLength)
}
