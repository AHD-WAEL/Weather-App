package eg.gov.iti.jets.weather.alert.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eg.gov.iti.jets.weather.databinding.FragmentAlertBinding

class AlertFragment : Fragment() {

    private lateinit var binding: FragmentAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }
}