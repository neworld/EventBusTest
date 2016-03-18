package lt.neworld.eventbustest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        single_event_bus_test.setOnClickListener { singleEventBusTest() }
        multi_event_bus_test.setOnClickListener { multiEventBusTest() }
    }

    private fun multiEventBusTest() {
        val (totalTime, processTime) = MultiEventBus().run()
        Toast.makeText(this, "MultiEvent test total time: ${totalTime / 1000} ms and process time: ${processTime / 1000} ms", LENGTH_LONG).show()
    }

    private fun singleEventBusTest() {
        val (totalTime, processTime) = SingleEventBus().run()
        Toast.makeText(this, "Single test total time: ${totalTime / 1000} ms and process time: ${processTime / 1000} ms", LENGTH_LONG).show()
    }
}
