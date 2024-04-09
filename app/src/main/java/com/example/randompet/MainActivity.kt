package com.example.randompet
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ScrollView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var pokemonName= mutableListOf<String>()
    var pokemonAbilities = mutableListOf<List<String>>()
    var pokemonType = mutableListOf<String>()
    var pokemonImg = mutableListOf<String>()
    private lateinit var rvPikachu: RecyclerView
    private lateinit var adapter: PikachuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchRandomPokemon(20)

        // Initialize RecyclerView
        rvPikachu = findViewById(R.id.pokemon_list)


        // Initialize and set up the RecyclerView adapter
        adapter = PikachuAdapter(pokemonName, pokemonImg, pokemonAbilities,pokemonType)
        rvPikachu.adapter = adapter
        rvPikachu.layoutManager = LinearLayoutManager(this)
        rvPikachu.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
        adapter.notifyDataSetChanged()
        val button = findViewById<Button>(R.id.buttonRandom)
        getNext(button)
    }

    private fun fetchRandomPokemon(count:Int){
        val offset = Random.nextInt(100)
        val apiLink = "https://pokeapi.co/api/v2/pokemon?limit=$count&offset=$offset"
        val client = AsyncHttpClient()


        client[apiLink, object : JsonHttpResponseHandler(){

            override fun onSuccess(statusCode:Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val results = json.jsonObject.getJSONArray("results")

                for (i in 0 until results.length()) {
                    val pokemon = results.getJSONObject(i)
                    val pokemonNames = pokemon.getString("name")
                    val pokemonUrl = pokemon.getString("url")

                    // Fetch detailed information for each Pokémon
                    fetchPokemonDetails(pokemonNames, pokemonUrl)
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Pokemon", errorResponse)
            }
    }]
    }


    private fun fetchPokemonDetails(name: String, url: String) {
        val client = AsyncHttpClient()

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                // Extract and parse the detailed information for the Pokémon
                pokemonType.add(json.jsonObject.getJSONArray("types")
                    .getJSONObject(0).getJSONObject("type").getString("name"))
                pokemonImg.add(json.jsonObject.getJSONObject("sprites").getString("front_default"))
                pokemonName.add(json.jsonObject.getString("name"))

                var abilitiesArray = json.jsonObject.getJSONArray("abilities")
                val abilitiesList = mutableListOf<String>()

                for (i in 0 until abilitiesArray.length()) {
                    val abilityObject = abilitiesArray.getJSONObject(i).getJSONObject("ability")
                    val abilityName = abilityObject.getString("name")
                    abilitiesList.add(abilityName)
                }

                pokemonAbilities.add(abilitiesList)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                // Handle API request failure
                Log.d("Pokemon Error", errorResponse)
            }
        }]
    }
    private fun getNext(button: Button) {

        button.setOnClickListener{
            pokemonName.clear()
            pokemonImg.clear()
            pokemonAbilities.clear()
            pokemonType.clear()
            adapter.notifyDataSetChanged()
            fetchRandomPokemon(20)
        }
    }
}


