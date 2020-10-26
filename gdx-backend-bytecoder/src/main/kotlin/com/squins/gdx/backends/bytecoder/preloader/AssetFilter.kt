package com.squins.gdx.backends.bytecoder.preloader

interface AssetFilter {
    enum class AssetType(val code: String) {
        Image("i"), Audio("a"), Text("t"), Binary("b"), Directory("d");

    }

    /** @param file the file to filter
     * @param isDirectory whether the file is a directory
     * @return whether to include the file in the war/ folder or not.
     */
    fun accept(file: String?, isDirectory: Boolean): Boolean

    /**
     *
     * @param file the file to filter
     * @return whether to preload the file, or to lazy load it via AssetManager
     */
    fun preload(file: String?): Boolean

    /** @param file the file to get the type for
     * @return the type of the file, one of [AssetType]
     */
    fun getType(file: String?): AssetType?

    /** @param file the file to get the bundle name for
     * @return the name of the bundle to which this file should be added
     */
    fun getBundleName(file: String?): String?
}