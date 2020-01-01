package com.aminography.foursquareapp.core.tools

import java.util.regex.Pattern

/**
 * A utility class contains text manipulation functions
 *
 * @author aminography
 */

private val pattern: Pattern = Pattern.compile("\\((.*?)\\)")

/**
 * Helps to extract part of the venue name which is returned by the service.
 *
 * @param venueName the name of the venue
 * @return pair of the venue name parts
 */
fun splitVenueName(venueName: String): Pair<String, String> {
    var first = ""
    var second = ""
    val split = venueName.split("|")
    when (split.size) {
        1 -> {
            if (venueName.contains("(") && venueName.contains(")")) {
                val matcher = pattern.matcher(venueName)
                if (matcher.find()) {
                    second = matcher.group(1) ?: ""
                }
                first = venueName.replace(" \\((.*?)\\)".toRegex(), "")
            } else {
                first = venueName
            }
        }
        2 -> {
            first = split[0]
            second = split[1]
            if (second.contains("(") && second.contains(")")) {
                val matcher = pattern.matcher(venueName)
                if (matcher.find()) {
                    second = matcher.group(1) ?: ""
                }
            } else {
                // nothing
            }
        }
    }
    return Pair(first.trim(), second.trim())
}