package ru.spbstu.kspt.task1;

import java.util.ArrayList;

public class Polinom {

    private ArrayList<Integer> coef;
    private String function;
    private int order;

    public Polinom(int order, String function) {
        this.coef = new ArrayList<>();
        this.order = order;
        if (this.order < 0) {
            throw new UnsupportedOperationException("Порядок полинома не может быть меньше нуля");
        }
        this.function = function;
        if (!this.function.equals("")) {
            String[] parts = this.function.split(";");
            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].isEmpty())
                    this.coef.add(Integer.parseInt(parts[i]));
            }
        } else {
            for (int i = 0; i <= this.order; i++) {
                this.coef.add(0);
            }
        }
    }

    private Polinom min(Polinom pol) {
        String minFunction = (this.order < pol.order) ? this.function : pol.function;
        return new Polinom(Math.min(this.order, pol.order), minFunction);
    }

    private Polinom aligment(Polinom pol) {
        Polinom result = this.min(pol);
        for (int i = 0; i < Math.abs(this.order - pol.order); i++) {
            result.coef.add(i, 0);
        }
        result.function = result.toString();
        return result;
    }

    public Polinom plus(Polinom pol) {
        Polinom maxPol = (this.order < pol.order) ? pol : this;
        Polinom minPol = this.aligment(pol);
        int end = maxPol.order;
        Polinom result = new Polinom(end, "");
        for (int i = 0; i <= end; i++) {
            result.coef.set(i, (maxPol.coef.get(i) + minPol.coef.get(i)));
        }
        result.function = result.toString();
        return result;
    }

    public Polinom minus(Polinom pol) {
        Polinom minuend, subtrahend;
        if (this.order > pol.order) {
            minuend = this;
            subtrahend = this.aligment(pol);
        } else {
            minuend = this.aligment(pol);
            subtrahend = pol;
        }
        int end = Math.max(this.order, pol.order);
        Polinom result = new Polinom(end, "");
        for (int i = 0; i <= end; i++) {
            result.coef.set(i, (minuend.coef.get(i) - subtrahend.coef.get(i)));
        }
        result.function = result.toString();
        return result;
    }

    public Polinom multiplication(Polinom pol) {
        int k;
        Polinom result = new Polinom(this.order + pol.order, "");
        for (int i = this.order; i >= 0; i--) {
            k = i + pol.order;
            for (int j = pol.order; j >= 0; j--) {
                result.coef.set(k, (this.coef.get(i) * pol.coef.get(j)) + result.coef.get(k));
                k--;
            }
        }
        result.function = result.toString();
        return result;
    }

    public int computation(int x) {
        int result = 0;
        for (int i = 0; i <= this.order; i++) {
            result += Math.pow(x, i) * this.coef.get(this.coef.size() - i - 1);
        }
        return result;
    }

    public Polinom quotient(Polinom pol) {
        if (this.order < pol.order) {
            throw new UnsupportedOperationException("Порядок делимого не может быть меньше порядка делителя");
        }
        Polinom result = new Polinom(this.order - pol.order, "");
        ArrayList<Double> dividend = new ArrayList<>();
        ArrayList<Double> divider = new ArrayList<>();
        ArrayList<Double> resultToDouble = new ArrayList<>(this.order - pol.order + 1);
        for (int i = 0; i <= this.order; i++) {
            dividend.add(i, this.coef.get(i).doubleValue());
            if (i <= pol.order) divider.add(i, pol.coef.get(i).doubleValue());
        }
        int leadingMember = 0;
        int k = 0;
        while (leadingMember <= pol.order) {
            resultToDouble.add(leadingMember, dividend.get(leadingMember) / divider.get(0));
            for (int i = leadingMember; i <= pol.order; i++) {
                dividend.set(i, dividend.get(i) - (resultToDouble.get(leadingMember) * divider.get(k)));
                k++;
            }
            k = 0;
            leadingMember++;
        }

        for (int i = 0; i <= result.order; i++) {
            result.coef.set(i, (int) Math.round(resultToDouble.get(i)));
        }
        result.function = result.toString();
        return result;
    }

    private Polinom cleaning() {
        ArrayList<Integer> resultCoef = new ArrayList<>();
        for (int i = 0; i <= this.order; i++) {
            if (this.coef.get(i) != 0) {
                for (int j = i; j <= this.order; j++) {
                    resultCoef.add(this.coef.get(j));
                }
                break;
            }
            if (i == this.order) {
                Polinom resultZero = new Polinom(0, "0");
                resultZero.coef.add(0);
                return resultZero;
            }
        }
        Polinom result = new Polinom(resultCoef.size() - 1, "");
        for (int i = 0; i <= result.order; i++) {
            result.coef.set(i, resultCoef.get(i));
        }
        result.function = result.toString();
        return result;
    }

    public Polinom remainder(Polinom pol) {
        if (this.order < pol.order) {
            throw new UnsupportedOperationException("Порядок делимого не может быть меньше порядка делителя");
        }
        Polinom result = new Polinom(pol.order - 1, "");
        ArrayList<Double> dividend = new ArrayList<>();
        ArrayList<Double> divider = new ArrayList<>();
        ArrayList<Double> resultToDouble = new ArrayList<>(this.order - pol.order + 1);
        for (int i = 0; i <= this.order; i++) {
            dividend.add(i, this.coef.get(i).doubleValue());
            if (i <= pol.order) divider.add(i, pol.coef.get(i).doubleValue());
        }
        int leadingMember = 0;
        int k = 0;
        int counter = pol.order;
        while (counter <= this.order) {
            resultToDouble.add(leadingMember, dividend.get(leadingMember) / divider.get(0));
            for (int i = leadingMember; i <= counter; i++) {
                dividend.set(i, dividend.get(i) - (resultToDouble.get(leadingMember) * divider.get(k)));
                k++;
            }
            k = 0;
            leadingMember++;
            counter++;
        }
        int q = dividend.size() - 1;
        for (int i = result.order; i >= 0; i--) {
            result.coef.set(i, (int) Math.round(dividend.get(q)));
            q--;
        }
        result = result.cleaning();
        result.function = result.toString();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.order; i++) {
            result.append(this.coef.get(i)).append(";");
        }
        result.append(this.coef.get(this.order));
        return result.toString();
    }

    @Override
    public boolean equals(Object object) {
        Polinom pol = (Polinom) object;
        if (object.getClass() != this.getClass() || this.order != pol.order) return false;
        double a, b;
        if (this.coef.get(0) == 0 && pol.coef.get(0) == 0) return true;
        if ((this.coef.get(0) % pol.coef.get(0)) == 0) {
            a = this.coef.get(0) / pol.coef.get(0);
        } else return false;
        for (int i = 1; i <= this.order; i++) {
            if (pol.coef.get(i) != 0) {
                if ((this.coef.get(i) % pol.coef.get(i)) == 0)
                    b = this.coef.get(i) / pol.coef.get(i);
                else return false;
                if (a != b) return false;
            } else {
                if (this.coef.get(i) != 0) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 11;
        int result;
        result = prime * this.function.hashCode();
        return result;
    }

    public static void main(String[] args) {
    }
}
