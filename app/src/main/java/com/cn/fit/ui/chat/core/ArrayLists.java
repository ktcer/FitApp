package com.cn.fit.ui.chat.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.cn.fit.ui.chat.common.utils.DemoUtils;
import com.cn.fit.ui.chat.core.comparator.PyComparator;
import com.cn.fit.ui.chat.ui.contact.ECContacts;

/**
 * com.cn.aihu.ui.chat.core in ECDemo_Android
 * Created by Jorstin on 2015/3/21.
 */
public class ArrayLists<E> extends ArrayList<E> {

    /**
     *
     */
    private static final long serialVersionUID = 7136125303151240526L;

    private PyComparator pyComparatornew = new PyComparator();

    private HashMap<String, ECContacts> indexed = new HashMap<String, ECContacts>();

    @Override
    public boolean add(E object) {
        super.add(object);

        try {
            ECContacts simpleContact = (ECContacts) object;
            List<Phone> phoneList = simpleContact.getPhoneList();
            if (phoneList != null) {
                int size = phoneList.size();
                for (int i = 0; i < size; i++) {
                    Phone phone = phoneList.get(i);
                    if (phone != null) {
                        String phoneNumber = phone.getPhoneNum();
                        if (phoneNumber != null && phoneNumber.length() > 0) {
                            synchronized (this) {
                                this.indexed.put(getPhoneNumber(phoneNumber), simpleContact);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private String getPhoneNumber(String phoneNumber) {
        return DemoUtils.formatPhone(phoneNumber);
    }

    @Override
    public void clear() {
        super.clear();
        try {
            this.indexed.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean remove(Object object) {
        boolean flag = super.remove(object);

        try {
            ECContacts simpleContact = (ECContacts) object;
            List<Phone> phoneList = simpleContact.getPhoneList();
            if (phoneList != null) {
                int size = phoneList.size();
                for (int i = 0; i < size; i++) {
                    Phone phone = phoneList.get(i);
                    if (phone != null) {
                        String phoneNumber = phone.getPhoneNum();
                        if (phoneNumber != null && phoneNumber.length() > 0) {
                            synchronized (this) {
                                this.indexed.remove(getPhoneNumber(phoneNumber));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public ECContacts getSimpleContactById(long id) {
        for (int i = 0; i < size(); i++) {
            ECContacts sc = (ECContacts) get(i);
            if (sc.getId() == id) {
                return sc;
            }
        }
        return null;
    }

    public ECContacts getValueByPhone(String phoneNumber) {
        String number = getPhoneNumber(phoneNumber);
        if (this.indexed != null) {
            return this.indexed.get(number);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        Collections.sort((List) this, pyComparatornew);
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        if (true)
            throw new RuntimeException("can't invoked this method.");

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        if (true)
            throw new RuntimeException("can't invoked this method.");

        return true;
    }

    @Override
    public E remove(int index) {
        if (true)
            throw new RuntimeException("can't invoked this method.");

        return null;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (true)
            throw new RuntimeException("can't invoked this method.");

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (true)
            throw new RuntimeException("can't invoked this method.");

        return true;
    }

}
