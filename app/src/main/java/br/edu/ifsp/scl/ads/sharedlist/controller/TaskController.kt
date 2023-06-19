package br.edu.ifsp.scl.ads.sharedlist.controller

import br.edu.ifsp.scl.ads.sharedlist.view.MainActivity
import br.edu.ifsp.scl.ads.sharedlist.model.Task
import br.edu.ifsp.scl.ads.sharedlist.model.TaskDAO
import br.edu.ifsp.scl.ads.sharedlist.model.TaskFirebaseDAO

class TaskController (private val mainActivity: MainActivity){
    private val FireBaseDAO: TaskDAO = TaskFirebaseDAO()

    fun insertTask(task: Task) {
        Thread {
            FireBaseDAO.createTask(task)
        }.start()
    }
    fun getTasks() {
        Thread {
            mainActivity.runOnUiThread {
                mainActivity.updateTaskList(FireBaseDAO.retrieveTasks())
            }
        }.start()
    }
    fun editTask(task: Task) {
        Thread {
            FireBaseDAO.updateTask(task)
        }.start()
    }
    fun removeTask(task: Task) {
        Thread {
            FireBaseDAO.deleteTask(task)
        }.start()
    }

}