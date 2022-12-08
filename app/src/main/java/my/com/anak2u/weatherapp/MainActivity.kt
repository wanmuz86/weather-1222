package my.com.anak2u.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var weatherTextView: TextView;
    lateinit var tempTextView:TextView;
    lateinit var pressureTextView:TextView;
    lateinit var humidityTextView:TextView;
    lateinit var sunriseTextView: TextView;
    lateinit var sunsetTextView: TextView;
    lateinit var cityEditText: EditText;
    lateinit var weatherImageView: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTextView = findViewById(R.id.weatherTextView)
        tempTextView = findViewById(R.id.tempTextView)
        pressureTextView = findViewById(R.id.pressureTextView)
        humidityTextView = findViewById(R.id.humidityTextView)
        sunriseTextView = findViewById(R.id.sunriseTextView)
        sunsetTextView = findViewById(R.id.sunsetTextView)
        cityEditText = findViewById(R.id.cityEditText)
        weatherImageView = findViewById(R.id.weatherImageView)
    }

    fun getWeather(view: View) {
        // Instantiate the RequestQueue.
        if (cityEditText.text.toString()!="") {
            val queue = Volley.newRequestQueue(this)
            val url =
                "https://api.openweathermap.org/data/2.5/weather?q=${cityEditText.text.toString()}&appid=9fd7a449d055dba26a982a3220f32aa2"

// Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Display the first 500 characters of the response string.
                    println("Response is: $response")
                    var responseObject = JSONObject(response)
                    var temperature = responseObject.getJSONObject("main").getDouble("temp")- 273.15
                    tempTextView.text = String.format("%.2f C",temperature)

                    //[] getJSONArray
                    // {} getJSONObject

                    var weather = responseObject.getJSONArray("weather").getJSONObject(0)
                        .getString("main")
                    weatherTextView.text = weather

                    var pressure = responseObject.getJSONObject("main").getInt("pressure")
                    pressureTextView.text = "$pressure hPa"

                    var humidity = responseObject.getJSONObject("main").getInt("humidity")
                    humidityTextView.text = "$humidity %"

                    var sunset = responseObject.getJSONObject("sys").getLong("sunset")
                    val sunsetdate = Date(sunset * 1000L)
                    val format = SimpleDateFormat("HH:mm:ss")
                    val sunsetTime = format.format(sunsetdate)
                    sunsetTextView.text = "$sunsetTime"

                    var sunrise = responseObject.getJSONObject("sys").getLong("sunrise")
                    val sunrisedate = Date(sunrise * 1000L)
                    val sunriseTime = format.format(sunrisedate)
                    sunriseTextView.text = "$sunriseTime"

                    var iconId =
                        responseObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                    println(iconId)
                    var imageUrl = "https://openweathermap.org/img/wn/$iconId@2x.png"
                    Glide.with(this@MainActivity).load(imageUrl).into(weatherImageView)


                },
                Response.ErrorListener { println("That didn't work!") })

// Add the request to the RequestQueue.
            queue.add(stringRequest)
        }
        else {
            Toast.makeText(this@MainActivity, "Please enter city name",Toast.LENGTH_LONG).show()
        }
    }
}