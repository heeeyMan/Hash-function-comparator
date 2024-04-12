package com.example.hfc.calculate

object HashFunction {

    fun calculateFunction(message: String): String {
        update(message)
        return digest().toString()
    }
    @OptIn(ExperimentalUnsignedTypes::class)
    private val K: UIntArray = uintArrayOf(
        0x428a2f98u, 0x71374491u, 0xb5c0fbcfu, 0xe9b5dba5u, 0x3956c25bu, 0x59f111f1u, 0x923f82a4u, 0xab1c5ed5u,
        0xd807aa98u, 0x12835b01u, 0x243185beu, 0x550c7dc3u, 0x72be5d74u, 0x80deb1feu, 0x9bdc06a7u, 0xc19bf174u,
        0xe49b69c1u, 0xefbe4786u, 0x0fc19dc6u, 0x240ca1ccu, 0x2de92c6fu, 0x4a7484aau, 0x5cb0a9dcu, 0x76f988dau,
        0x983e5152u, 0xa831c66du, 0xb00327c8u, 0xbf597fc7u, 0xc6e00bf3u, 0xd5a79147u, 0x06ca6351u, 0x14292967u,
        0x27b70a85u, 0x2e1b2138u, 0x4d2c6dfcu, 0x53380d13u, 0x650a7354u, 0x766a0abbu, 0x81c2c92eu, 0x92722c85u,
        0xa2bfe8a1u, 0xa81a664bu, 0xc24b8b70u, 0xc76c51a3u, 0xd192e819u, 0xd6990624u, 0xf40e3585u, 0x106aa070u,
        0x19a4c116u, 0x1e376c08u, 0x2748774cu, 0x34b0bcb5u, 0x391c0cb3u, 0x4ed8aa4au, 0x5b9cca4fu, 0x682e6ff3u,
        0x748f82eeu, 0x78a5636fu, 0x84c87814u, 0x8cc70208u, 0x90befffau, 0xa4506cebu, 0xbef9a3f7u, 0xc67178f2u
    )

    private var blocklen: UInt = 0u
    private var bitlen: ULong = 0u
    @OptIn(ExperimentalUnsignedTypes::class)
    private val m_state: UIntArray = uintArrayOf(
        0x6a09e667u, 0xbb67ae85u, 0x3c6ef372u, 0xa54ff53au,
        0x510e527fu, 0x9b05688cu, 0x1f83d9abu, 0x5be0cd19u
    )
    @OptIn(ExperimentalUnsignedTypes::class)
    private val m_data: UByteArray = UByteArray(64)

    @OptIn(ExperimentalUnsignedTypes::class)
    fun update(data: ByteArray, length: Int) {
        for (i in 0 until length) {
            m_data[blocklen.toInt()] = data[i].toUByte()
            blocklen++
            if (blocklen == 64u) {
                transform()
                bitlen += 512u
                blocklen = 0u
            }
        }
    }

    private fun update(data: String) {
        update(data.toByteArray(), data.length)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun transform() {
        val m = UIntArray(64)
        val state = m_state.copyOf()

        for (i in 0 until 16) {
            m[i] = (m_data[i * 4].toUInt() shl 24) or
                    (m_data[i * 4 + 1].toUInt() shl 16) or
                    (m_data[i * 4 + 2].toUInt() shl 8) or
                    m_data[i * 4 + 3].toUInt()
        }

        for (k in 16 until 64) {
            m[k] = sig1(m[k - 2]) + m[k - 7] + sig0(m[k - 15]) + m[k - 16]
        }

        for (i in 0 until 64) {
            val maj = majority(state[0], state[1], state[2])
            val xorA = rotr(state[0], 2u) xor rotr(state[0], 13u) xor rotr(state[0], 22u)

            val ch = choose(state[4], state[5], state[6])
            val xorE = rotr(state[4], 6u) xor rotr(state[4], 11u) xor rotr(state[4], 25u)

            val sum = m[i] + K[i] + state[7] + ch + xorE
            val newA = xorA + maj + sum
            val newE = state[3] + sum

            state[7] = state[6]
            state[6] = state[5]
            state[5] = state[4]
            state[4] = newE
            state[3] = state[2]
            state[2] = state[1]
            state[1] = state[0]
            state[0] = newA
        }

        for (i in 0 until 8) {
            m_state[i] += state[i]
        }
    }

    private fun digest(): ByteArray {
        val hash = ByteArray(32)
        pad()
        revert(hash)
        return hash
    }

    private fun rotr(x: UInt, n: UInt): UInt {
        return (x shr n.toInt()) or (x shl (32 - n.toInt()))
    }

    private fun choose(e: UInt, f: UInt, g: UInt): UInt {
        return (e and f) xor (e.inv() and g)
    }

    private fun majority(a: UInt, b: UInt, c: UInt): UInt {
        return (a and (b or c)) or (b and c)
    }

    private fun sig0(x: UInt): UInt {
        return rotr(x, 7u) xor rotr(x, 18u) xor (x shr 3)
    }

    private fun sig1(x: UInt): UInt {
        return rotr(x, 17u) xor rotr(x, 19u) xor (x shr 10)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun pad() {
        var i = blocklen.toInt()
        val end = if (blocklen < 56u) 56 else 64

        m_data[i++] = 0x80u // Append a bit 1
        while (i < end) {
            m_data[i++] = 0x00u // Pad with zeros
        }

        if (blocklen >= 56u) {
            transform()
            m_data.fill(0u, 0, 56)
        }

        bitlen += blocklen * 8u
        m_data[63] = bitlen.toUByte()
        m_data[62] = (bitlen shr 8).toUByte()
        m_data[61] = (bitlen shr 16).toUByte()
        m_data[60] = (bitlen shr 24).toUByte()
        m_data[59] = (bitlen shr 32).toUByte()
        m_data[58] = (bitlen shr 40).toUByte()
        m_data[57] = (bitlen shr 48).toUByte()
        m_data[56] = (bitlen shr 56).toUByte()
        transform()
    }
    @OptIn(ExperimentalUnsignedTypes::class)
    fun revert(hash: ByteArray) {
        for (i in 0 until 4) {
            for (j in 0 until 8) {
                hash[i + (j * 4)] = ((m_state[j] shr (24 - i * 8)) and 255u).toByte()
            }
        }
    }
}