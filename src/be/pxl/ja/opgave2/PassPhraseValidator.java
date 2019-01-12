package be.pxl.ja.opgave2;

import java.util.List;

public class PassPhraseValidator<T> extends Thread {

    private List<T> passPhrase;

    public PassPhraseValidator(List<T> arrayList) {
        this.passPhrase = arrayList;
    }

    public boolean isValid() {
        for (int i = 0; i < passPhrase.size(); i++) {
            for (int j = i + 1; j < passPhrase.size(); j++) {
                if (passPhrase.get(i).equals(passPhrase.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<T> getPassPhrase() {
        return passPhrase;
    }

    @Override
    public void run() {
        isValid();
    }
}
