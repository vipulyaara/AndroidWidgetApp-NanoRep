package nanorep.nanowidget.interfaces;

import java.util.List;

/**
 * Created by noat on 03/11/2016.
 */

public interface CRUDAdapterInterface<T> {

    T getItem(int position);
    void addItem(T item);
    void removeItem(T item);
    void addItems(List<T> items);
    void clearList();
    void updateItem(int pos, T item);
    void showItem(T item, int itemPosition);
    List<T> getItems();

    void removeItem(int currentItemPosition);
}
