package br.edu.ifsp.scl.ads.sharedlist.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.ads.sharedlist.R
import br.edu.ifsp.scl.ads.sharedlist.adapter.OnTaskClickListener
import br.edu.ifsp.scl.ads.sharedlist.adapter.TaskAdapter
import br.edu.ifsp.scl.ads.sharedlist.controller.TaskController
import br.edu.ifsp.scl.ads.sharedlist.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), OnTaskClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val taskList: MutableList<Task> = mutableListOf()
    private val taskAdapter: TaskAdapter by lazy {
        TaskAdapter(taskList, this)

    }
    private lateinit var carl: ActivityResultLauncher<Intent>

    private val taskController: TaskController by lazy {
        TaskController(this)
    }
    lateinit var updateViewsHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.tasks)

        taskController.getTasks()
        amb.tasksRv.layoutManager = LinearLayoutManager(this)
        amb.tasksRv.adapter = taskAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if ( result.resultCode == RESULT_OK) {
                val task = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_TASK, Task::class.java)
                } else {
                    result.data?.getParcelableExtra(EXTRA_TASK)
                }
                task?.let {_task ->
                    val position = taskList.indexOfFirst { it.id == _task.id}
                    if (position != -1) {
                        taskList[position] = _task
                        taskController.editTask(_task)
                        Toast.makeText(this, getString(R.string.task_completed), Toast.LENGTH_LONG).show()
                    } else {
                        if(taskAlreadyExists(_task)){
                            Toast.makeText(this, getString(R.string.task_already_exists), Toast.LENGTH_LONG).show()
                        } else{
                            taskController.insertTask(_task)
                            Toast.makeText(this, getString(R.string.task_inserted), Toast.LENGTH_LONG).show()
                        }
                    }
                    taskController.getTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            }
        }
        updateViewsHandler = Handler(Looper.myLooper()!!) {
            taskController.getTasks()
            true
        }
        updateViewsHandler.sendMessageDelayed(Message(),3000)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addTaskMi -> {
                carl.launch(Intent(this, TaskActivity::class.java))
                true
            }
            R.id.signOutMi -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient.signOut()
                finish()
                true
            }
            else -> false
        }
    }

    fun updateTaskList(_taskList: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(_taskList)
        taskAdapter.notifyDataSetChanged()
    }

    override fun onTileTaskClick(position: Int) {
        val task = taskList[position]
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        intent.putExtra(EXTRA_VIEW_TASK, true)
        carl.launch(intent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val task = taskList[position]
        if(task.isFinished) {
            Toast.makeText(this, getString(R.string.task_already_finished), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(EXTRA_TASK, task)
        carl.launch(intent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val task = taskList[position]
        if(task.isFinished) {
            Toast.makeText(this, getString(R.string.task_already_finished), Toast.LENGTH_SHORT).show()
            return
        }
        taskList.removeAt(position)
        taskController.removeTask(task)
        taskAdapter.notifyDataSetChanged()
        Toast.makeText(this, getString(R.string.task_removed), Toast.LENGTH_SHORT).show()
    }

    override fun onFinishMenuItemClick(position: Int) {
        val task = taskList[position]
        task.isFinished = true
        task.finishedBy = FirebaseAuth.getInstance().currentUser?.email.toString()
        taskController.editTask(task)
        taskAdapter.notifyItemChanged(position)
        Toast.makeText(this, getString(R.string.task_concluded), Toast.LENGTH_SHORT).show()
    }


    private fun taskAlreadyExists(_task: Task): Boolean = taskList.any { it.id == _task.id }
}