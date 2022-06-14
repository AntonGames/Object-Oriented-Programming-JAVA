package com.main.lab_3;

public abstract class group_functions {

    abstract void filter_manager();
    abstract void remove_filter();

    public static String[] string_splitting(final String line, final char delimiter)
    {
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter);

        while (j >= 0)
        {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i);
        }

        temp[wordCount++] = line.substring(i);

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }
}
