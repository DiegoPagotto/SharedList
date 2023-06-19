package br.edu.ifsp.scl.ads.sharedlist.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import br.edu.ifsp.scl.ads.sharedlist.databinding.ActivityTaskBinding
import br.edu.ifsp.scl.ads.sharedlist.model.Task
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date

class TaskActivity : BaseActivity() {
    private val acb : ActivityTaskBinding by lazy {
        ActivityTaskBinding.inflate(layoutInflater)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)
        supportActionBar?.subtitle = "Task information"

        val receivedTask = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_TASK, Task::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_TASK)
        }
        receivedTask?.let{_receivedTask ->
            with(acb){
                with(_receivedTask){
                    titleEt.setText(title)
                    descriptionEt.setText(description)
                    creationDateEt.setText(creationDate)
                    expectedFinishDateEt.setText(expectedFinishDate)
                    createdByEt.setText(createdBy)
                    finishedByEt.setText(finishedBy)
                }
            }
            val viewTask = intent.getBooleanExtra(EXTRA_VIEW_TASK, false)
            with(acb){
                titleLabelTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                titleEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                titleEt.isEnabled = !viewTask
                descriptionEt.isEnabled = !viewTask
                creationDateTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                creationDateEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                creationDateEt.isEnabled = !viewTask
                expectedFinishDateEt.isEnabled = !viewTask
                createdByTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                createdByEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                createdByEt.isEnabled = !viewTask
                saveBt.visibility = if(viewTask) View.GONE else View.VISIBLE
                if(receivedTask.isFinished){
                    finishedByTv.visibility = if(viewTask) View.VISIBLE else View.GONE
                    finishedByEt.visibility = if(viewTask) View.VISIBLE else View.GONE
                    finishedByEt.isEnabled = !viewTask
                }
            }
        }
        acb.saveBt.setOnClickListener{
            val task = Task(
                id = receivedTask?.id,
                title = acb.titleEt.text.toString(),
                description = acb.descriptionEt.text.toString(),
                creationDate = SimpleDateFormat("dd/MM/yyyy").format(Date()),
                expectedFinishDate = acb.expectedFinishDateEt.text.toString(),
                createdBy = FirebaseAuth.getInstance().currentUser?.email.toString()
            )

            val resultIntent = intent
            resultIntent.putExtra(EXTRA_TASK, task)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }
}