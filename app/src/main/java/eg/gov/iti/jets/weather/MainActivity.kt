package eg.gov.iti.jets.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weather.databinding.ActivityMainBinding
import eg.gov.iti.jets.weather.home.view.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.fabBtn.setOnClickListener {
            replaceFragment(HomeFragment())
        }

        binding.bottomNav.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.favourite -> replaceFragment(FavouriteFragment())
                R.id.alert -> replaceFragment(AlertFragment())
                R.id.setting -> replaceFragment(SettingFragment())

                else ->{}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}