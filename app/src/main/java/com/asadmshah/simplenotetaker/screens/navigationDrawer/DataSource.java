package com.asadmshah.simplenotetaker.screens.navigationDrawer;

import com.asadmshah.simplenotetaker.models.Tag;

interface DataSource {

    Tag getTag(int position);

    int getCount();
}
