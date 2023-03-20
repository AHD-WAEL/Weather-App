package eg.gov.iti.jets.weather


object Constants {
    const val baseUrl = "https://api.openweathermap.org/data/2.5/"
    const val apiKey = "b1ae6e2d300c78c2129844e69b0f041d"
    private const val imgURL = "https://openweathermap.org/img/wn/"

    fun getImage(position: String): String{
        return "$imgURL$position.png"
    }

    fun getTime(hour: Int): String{
        var time = hour
        val hourString:String
        if(time > 12)
        {
            time -= 12
            hourString = time.toString()+"pm"
        }
        else if(time==12) hourString = time.toString()+"pm"
        //else if(hour==0) hourString = "12am"
        else hourString = time.toString()+"am"
        return hourString
    }
}
