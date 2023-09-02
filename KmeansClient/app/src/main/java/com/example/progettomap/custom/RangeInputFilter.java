package com.example.progettomap.custom;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * <h2>La classe implementa un filtro per gli input numerici.</h2>
 */
public class RangeInputFilter implements InputFilter {
    /**
     * Valore minimo accettabile.
     */
    private final int minValue;
    /**
     * Valore massimo accettabile.
     */
    private final int maxValue;

    /**
     * <h4>Costruttore della classe</h4>.
     *
     * @param minValue Valore minimo accettabile.
     * @param maxValue Valore massimo accettabile.
     */
    public RangeInputFilter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * <h4>Controlla che l'input sia all'interno del range accettabile.</h4>
     *
     * @param source Input da controllare.
     * @param start  Indice di inizio dell'input.
     * @param end    Indice di fine dell'input.
     * @param dest   Testo di destinazione.
     * @param dstart Indice di inizio del testo di destinazione.
     * @param dend   Indice di fine del testo di destinazione.
     * @return Restituisce null se l'input è all'interno del range accettabile, altrimenti restituisce una stringa vuota.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            String newValue = dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length());
            int inputNumber = Integer.parseInt(newValue);
            if (inputNumber >= minValue && inputNumber <= maxValue) {
                return null;
            }
        } catch (NumberFormatException ignored) {
        }
        return "";
    }
}

