Implementation 

   Dependency
    
    implementation 'com.github.murinaa62:spins:1.0.2' 
 
   Add it in your root build.gradle at the end of repositories:
   
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  For flow:
  
    def coroutinesVersion = '1.4.2'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    
    
  Usage: 
  
  In XML: 
  
     <com.bcvbgfd.slotsLib.SlotRecyclerView
        android:id="@+id/slots"
        android:layout_width="match_parent"
        android:layout_height="350dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
        
   In code:
   
    @DrawableRes
    private val listOfImages = listOf(
        R.drawable.q01,
        R.drawable.q02
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.slots.init(listOfImages, callback = ::resultOfSpin)
        binding.spinButton.setOnClickListener {
            binding.slots.startSpin()
        }
        observeState()
    }
    
    private fun resultOfSpin(result: SpinResult){
        //make some amazing animation
    }
    private fun observeState() {
        val player = binding.slots.player
        player.currentMoney.mapLatest {
            Log.e("Player", "currentMoney = $it")
        }.launchIn(lifecycleScope)
        player.currentBet.mapLatest {
            Log.e("Player", "currentBet = $it")
        }.launchIn(lifecycleScope)
        player.notEnoughMoney.mapLatest {
            Log.e("Player", "error = $it")
        }.launchIn(lifecycleScope)
        binding.slots.isSpinning.mapLatest {
            Log.e("Player", "iaSpinning = $it")
        }.launchIn(lifecycleScope)
    }
    
   You can customize win multiplier, start money, start bet, bet step by adding them to constructor:
   
     val multi = mapOf(
            SpinResult.BIG to 3,
            SpinResult.JACKPOT to 100,
            SpinResult.SMALL to 2,
            SpinResult.LOOSE to 0
        )
        val player = Player(
            startMoney = 10_000,
            startBet = 500,
            step = 100,
            multi = multi
        )
        binding.slots.init(listOfImages, player = player, callback = ::resultOfSpin)
        
 Default values:
 
    startMoney: Int = 10_000,
    startBet: Int = 500,
    private val step: Int = 500,
    private val multi: Map<SpinResult, Int> = mapOf(
        SpinResult.BIG to 3,
        SpinResult.JACKPOT to 100,
        SpinResult.SMALL to 2,
        SpinResult.LOOSE to 0
    )
   
   
   
    
   
        
   
