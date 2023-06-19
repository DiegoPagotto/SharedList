package br.edu.ifsp.scl.ads.sharedlist.adapter

interface OnTaskClickListener {
    fun onTileTaskClick(position: Int)
    fun onEditMenuItemClick(position: Int)
    fun onRemoveMenuItemClick(position: Int)
    fun onFinishMenuItemClick(position: Int)
}