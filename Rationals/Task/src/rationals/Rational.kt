package rationals

import java.lang.StringBuilder
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.abs


class Rational(val numerator: BigInteger, val denominator: BigInteger): Comparable<Rational>{

    init {
        if (denominator == BigInteger.ZERO) throw java.lang.IllegalArgumentException()
    }

    operator fun plus(other: Rational): Rational {
        val mcm = denominator * other.denominator
        val sum = (numerator*mcm/denominator) + (other.numerator*mcm/other.denominator)
        return Rational(sum, mcm)
    }

    operator fun minus(other: Rational): Rational {
        val mcm = denominator * other.denominator
        val sum = (numerator*mcm/denominator) - (other.numerator*mcm/other.denominator)
        return Rational(sum, mcm)
    }

    operator fun unaryMinus(): Rational {
        return Rational(-numerator, denominator)
    }

    operator fun times(other: Rational): Rational {
        return Rational(numerator * other.numerator, denominator * other.denominator)
    }

    operator fun div(other: Rational): Rational {
        return Rational(numerator * other.denominator, denominator * other.numerator)
    }

    override operator fun compareTo(other: Rational): Int {
        var n1 = numerator.toDouble()
        n1 /= denominator.toDouble()

        var n2 = other.numerator.toDouble()
        n2 /= other.denominator.toDouble()

        return n1.compareTo(n2)
    }

    private fun toNormalizedForm(): Rational{
        val gcd = numerator.gcd(denominator)
        return Rational(numerator/gcd, denominator/gcd)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false

        var a = this.numerator.toBigDecimal()
        a = a.divide(this.denominator.toBigDecimal(), MathContext(5))
        var b = other.numerator.toBigDecimal()
        b = b.divide(other.denominator.toBigDecimal(), MathContext(5))
        return a == b
    }

    override fun toString(): String {

        val normalizedRational: Rational = toNormalizedForm()
        var normalizedNumerator = StringBuilder(normalizedRational.numerator.toString())
        val normalizedDenominator = StringBuilder(normalizedRational.denominator.toString())

        if (normalizedRational.numerator < BigInteger.ZERO && normalizedRational.denominator < BigInteger.ZERO){
            normalizedNumerator.deleteCharAt(0)      // Delete the - from numerator
            normalizedDenominator.deleteCharAt(0)    // Delete the - from denominator
        }else if(normalizedRational.numerator > BigInteger.ZERO && normalizedRational.denominator < BigInteger.ZERO){
            normalizedNumerator = StringBuilder("-$normalizedNumerator")      // Add - to numerator
            normalizedDenominator.deleteCharAt(0)    // and delete the - from denominator
        }

        if (abs((normalizedRational.denominator).toInt()) == 1)
            return "$normalizedNumerator"

        return "$normalizedNumerator/$normalizedDenominator"
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }

}


fun String.toRational(): Rational {
    val split = this.split('/')
    val numerator: BigInteger = split[0].toBigInteger()
    val denominator = split.elementAtOrElse(1){ "1" }.toBigInteger()
    return Rational(numerator, denominator)
}

infix fun Int.divBy(other: Int): Rational = Rational(this.toBigInteger(), other.toBigInteger())
infix fun Long.divBy(other: Long): Rational = Rational(this.toBigInteger(), other.toBigInteger())
infix fun BigInteger.divBy(other: BigInteger): Rational = Rational(this, other)


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}