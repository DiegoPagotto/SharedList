package br.edu.ifsp.scl.ads.sharedlist.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TaskFirebaseDAO : TaskDAO {
    private val TASKS_ROOT_NODE = "tasks"
    private val taskRtDbFbReference = Firebase.database.getReference(TASKS_ROOT_NODE)

    private val tasks: MutableList<Task> = mutableListOf()
    init{
        taskRtDbFbReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val task : Task ? = snapshot.getValue<Task>()
                task?.let { _task ->
                    if (!tasks.any { _task.title == it.title }) {
                        tasks.add(_task)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let { _task ->
                    tasks[tasks.indexOfFirst { _task.title == it.title }] = _task
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val task: Task? = snapshot.getValue<Task>()
                task?.let { _task ->
                    tasks.remove(_task)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })



        taskRtDbFbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tasks.clear()
                snapshot.getValue<HashMap<String, Task>>()?.values?.forEach {
                    tasks.add(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun createTask(task: Task) {
        createOrUpdateTask(task)
    }

    override fun retrieveTasks() = tasks;

    override fun updateTask(task: Task): Int {
        createOrUpdateTask(task)
        return 1
    }

    override fun deleteTask(task: Task): Int {
        taskRtDbFbReference.child(task.title).removeValue()
        return 1
    }

    private fun createOrUpdateTask(task: Task) = taskRtDbFbReference.child(task.title).setValue(task)
}