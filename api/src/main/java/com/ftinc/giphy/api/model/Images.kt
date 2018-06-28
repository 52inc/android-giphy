package com.ftinc.giphy.api.model


data class Images(
        val fixed_height: Image,
        val fixed_height_still: Image,
        val fixed_height_downsampled: Image,
        val fixed_width: Image,
        val fixed_width_still: Image,
        val fixed_width_downsampled: Image,
        val fixed_height_small: Image,
        val fixed_height_small_still: Image,
        val fixed_width_small: Image,
        val fixed_width_small_still: Image,
        val downsized: Image,
        val downsized_still: Image,
        val downsized_large: Image,
        val original: Image,
        val original_still: Image
)