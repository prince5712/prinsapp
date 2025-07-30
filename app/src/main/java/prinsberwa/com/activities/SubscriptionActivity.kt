package prinsberwa.com.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import prinsberwa.com.R
import prinsberwa.com.base.BaseActivity
import prinsberwa.com.databinding.ActivitySubscriptionBinding

class SubscriptionActivity : BaseActivity() {

    private lateinit var binding: ActivitySubscriptionBinding
    private var selectedPlan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        checkSubscriptionStatus()
    }

    private fun setupClickListeners() {
        // Set up card selection for monthly plan
        binding.cardMonthly.setOnClickListener {
            selectPlan("monthly")
            highlightSelectedCard(binding.cardMonthly, binding.cardYearly)
            binding.buttonSubscribe.isEnabled = true
        }

        // Set up card selection for yearly plan
        binding.cardYearly.setOnClickListener {
            selectPlan("yearly")
            highlightSelectedCard(binding.cardYearly, binding.cardMonthly)
            binding.buttonSubscribe.isEnabled = true
        }

        // Set up subscribe button
        binding.buttonSubscribe.setOnClickListener {
            if (selectedPlan != null) {
                startSubscriptionProcess()
            } else {
                Toast.makeText(this, "Please select a subscription plan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPlan(plan: String) {
        selectedPlan = plan
    }

    private fun highlightSelectedCard(selectedCard: CardView, unselectedCard: CardView) {
        // Change background color to indicate selection
        selectedCard.setCardBackgroundColor(getColor(R.color.gray))
        unselectedCard.setCardBackgroundColor(getColor(R.color.gray))
    }

    private fun startSubscriptionProcess() {
        // Show loading view
        binding.loadingView.visibility = View.VISIBLE

        // Simulate network call for subscription
        binding.buttonSubscribe.isEnabled = false

        // In a real app, you would initiate the actual subscription process here
        // For demo purposes, we'll just simulate a delay and show a result
        binding.root.postDelayed({
            binding.loadingView.visibility = View.GONE

            // For demo purposes, always succeed
            onSubscriptionSuccess()

            // In a real app, you would handle success or failure based on the API response
            // onSubscriptionFailure("Error message")
        }, 2000) // Simulate 2-second delay
    }

    private fun onSubscriptionSuccess() {
        val planType = if (selectedPlan == "monthly") "monthly" else "yearly"
        val planPrice = if (selectedPlan == "monthly") "$2.99" else "$24.99"

        binding.textStatus.text = "Successfully subscribed to $planType plan ($planPrice)"
        binding.textStatus.setBackgroundColor(getColor(R.color.dark))
        binding.textStatus.visibility = View.VISIBLE

        // Update UI to reflect subscribed state
        binding.buttonSubscribe.text = "Manage Subscription"
        binding.buttonSubscribe.isEnabled = true

        // In a real app, you would store the subscription status in SharedPreferences or a database
        saveSubscriptionStatus(true, selectedPlan!!)
    }

    private fun onSubscriptionFailure(errorMessage: String) {
        binding.textStatus.text = "Subscription failed: $errorMessage"
        binding.textStatus.setBackgroundColor(getColor(R.color.dark))
        binding.textStatus.visibility = View.VISIBLE
        binding.buttonSubscribe.isEnabled = true
    }

    private fun saveSubscriptionStatus(isSubscribed: Boolean, planType: String) {
        val sharedPreferences = getSharedPreferences("subscription_prefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("is_subscribed", isSubscribed)
            putString("plan_type", planType)
            putLong("subscription_date", System.currentTimeMillis())
            apply()
        }
    }

    private fun checkSubscriptionStatus() {
        val sharedPreferences = getSharedPreferences("subscription_prefs", MODE_PRIVATE)
        val isSubscribed = sharedPreferences.getBoolean("is_subscribed", false)

        if (isSubscribed) {
            // User is already subscribed
            val planType = sharedPreferences.getString("plan_type", "unknown")
            val planPrice = if (planType == "monthly") "$2.99" else "$24.99"

            binding.textStatus.text = "Currently subscribed to $planType plan ($planPrice)"
            binding.textStatus.setBackgroundColor(getColor(R.color.dark))
            binding.textStatus.visibility = View.VISIBLE

            binding.buttonSubscribe.text = "Manage Subscription"
            binding.buttonSubscribe.isEnabled = true

            // Select the appropriate card
            if (planType == "monthly") {
                selectPlan("monthly")
                highlightSelectedCard(binding.cardMonthly, binding.cardYearly)
            } else {
                selectPlan("yearly")
                highlightSelectedCard(binding.cardYearly, binding.cardMonthly)
            }
        }
    }
}