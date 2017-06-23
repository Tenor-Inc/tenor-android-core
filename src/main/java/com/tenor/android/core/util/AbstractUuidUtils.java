package com.tenor.android.core.util;

import java.util.UUID;

public abstract class AbstractUuidUtils {

    /**
     * Create a new uuid
     */
    public static String createUuid() {
        /*
         * GUID
         *
         * https://developer.android.com/training/articles/user-data-ids.html#working_with_instance_ids_&_guids
         *
         * Server prefer to have no hyphen for better space performance
         */
        return toHyphenlessString(UUID.randomUUID());
    }

    /**
     * Duplicated and modified from UUID#toString()
     * <p>
     * Returns a {@code String} object representing this {@code UUID}.
     * <p>
     * <p> The UUID string representation is as described by this BNF:
     * <blockquote><pre>
     * {@code
     * UUID                   = <time_low>
     *                          <time_mid>
     *                          <time_high_and_version>
     *                          <variant_and_sequence>
     *                          <node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               =
     *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     *       | "a" | "b" | "c" | "d" | "e" | "f"
     *       | "A" | "B" | "C" | "D" | "E" | "F"
     * }</pre></blockquote>
     *
     * @return A string representation of this {@code UUID}
     */
    private static String toHyphenlessString(UUID uuid) {
        return (digits(uuid.getMostSignificantBits() >> 32, 8)
                + digits(uuid.getMostSignificantBits() >> 16, 4)
                + digits(uuid.getMostSignificantBits(), 4)
                + digits(uuid.getLeastSignificantBits() >> 48, 4)
                + digits(uuid.getLeastSignificantBits(), 12));
    }

    /**
     * Duplicated from UUID#digits()
     * <p>
     * Returns val represented by the specified number of hex digits.
     */
    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }
}
