package br.edu.ifsp.scl.ads.sharedlist.adapter

import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.ads.sharedlist.databinding.TileTaskBinding
import br.edu.ifsp.scl.ads.sharedlist.model.Task

class TaskAdapter (
    private val taskList: MutableList<Task>,
    private val onTaskClickListener: OnTaskClickListener
    ): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
        inner class TaskViewHolder(tileTaskBinding: TileTaskBinding): RecyclerView.ViewHolder(tileTaskBinding.root), View.OnCreateContextMenuListener{
            val titleTv: TextView = tileTaskBinding.titleTv
            val finishDateTv: TextView = tileTaskBinding.finishDateTv
            var taskPosition= -1
            init{
                tileTaskBinding.root.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                menu?.add(Menu.NONE, 0,0, "Editar")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onEditMenuItemClick(taskPosition)
                    }
                    true
                }
                menu?.add(Menu.NONE, 1,1, "Remover")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onRemoveMenuItemClick(taskPosition)
                    }
                    true
                }
                menu?.add(Menu.NONE, 3,3, "Finalizar")?.setOnMenuItemClickListener {
                    if (taskPosition != -1){
                        onTaskClickListener.onFinishMenuItemClick(taskPosition)
                    }
                    true
                }
            }
        }
    override fun getItemCount(): Int = taskList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val tileTaskBinding = TileTaskBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(tileTaskBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        if (task.isFinished) {
            holder.itemView.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.titleTv.text = task.title
        holder.finishDateTv.text = task.expectedFinishDate.toString()
        holder.taskPosition = position

        holder.itemView.setOnClickListener {
            onTaskClickListener.onTileTaskClick(position)
        }
    }
}