package com.example.maydemniemkinh.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.maydemniemkinh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var guestCount = 0
    // Các biến trạng thái khác sẽ được thêm ở đây

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupEventListeners()
        updateUI()
    }

    private fun setupEventListeners() {
        binding.btnIncrement.setOnClickListener { incrementCounter() }
        binding.tvCounter.setOnClickListener { incrementCounter() }
        binding.btnDecrement.setOnClickListener { decrementCounter() }
        // Các listener khác sẽ được thêm ở đây
    }

    private fun incrementCounter() {
        // Tạm thời chỉ xử lý cho guest
        guestCount++
        updateUI()
    }

    private fun decrementCounter() {
        if (guestCount > 0) {
            guestCount--
            updateUI()
        }
    }

    private fun updateUI() {
        // Cập nhật số đếm trên giao diện
        binding.tvCounter.text = guestCount.toString()
        // Logic ẩn/hiện các thành phần khác sẽ ở đây
    }
}