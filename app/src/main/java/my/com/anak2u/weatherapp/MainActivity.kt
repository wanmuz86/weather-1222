package my.com.anak2u.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    lateinit var weatherTextView: TextView;
    lateinit var tempTextView:TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTextView = findViewById(R.id.weatherTextView)
        tempTextView = findViewById(R.id.tempTextView)
    }

    fun getWeather(view: View) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?q=Kuala%20Lumpur&appid=9fd7a449d055dba26a982a3220f32aa2"

// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                // Display the first 500 characters of the response string.
                println("Response is: $response")
                var responseObject = JSONObject(response)
                var temperature = responseObject.getJSONObject("main").getDouble("temp")
                tempTextView.text = "${temperature - 273.15} C"

                //[] getJSONArray
                // {} getJSONObject

                var weather = responseObject.getJSONArray("weather").getJSONObject(0)
                    .getString("main")
                weatherTextView.text = weather



            },
            Response.ErrorListener { println("That didn't work!") })

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }
}