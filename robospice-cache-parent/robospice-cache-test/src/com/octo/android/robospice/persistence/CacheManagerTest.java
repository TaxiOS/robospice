package com.octo.android.robospice.persistence;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;

@SmallTest
public class CacheManagerTest extends AndroidTestCase {
    private static final String TEST_PERSISTED_STRING = "TEST";
    private static final Integer TEST_PERSISTED_INTEGER = Integer.valueOf( 0 );

    private CacheManager cacheManager;

    @Override
    protected void setUp() throws Exception {
        cacheManager = new CacheManager();
    }

    public void testEmptyDataPersistenceManager() {
        try {
            cacheManager.getObjectPersister( Object.class );
            fail( "No data class persistence manager should have been found as none had been registered" );
        } catch ( Exception ex ) {
            assertTrue( true );
        }
    }

    public void testRegisterDataClassPersistenceManager() {
        MockStringPersistenceManager mockStringPersistenceManager = new MockStringPersistenceManager();
        cacheManager.addPersister( mockStringPersistenceManager );
        ObjectPersister< ? > actual = cacheManager.getObjectPersister( String.class );
        assertEquals( mockStringPersistenceManager, actual );
    }

    public void testGetDataClassPersistenceManager_returns_CacheManagerBusElement_in_order() {
        // register a data class persistence manager first
        MockStringPersistenceManager mockStringPersistenceManager = new MockStringPersistenceManager();
        cacheManager.addPersister( mockStringPersistenceManager );

        // register a second data class persistence manager
        MockStringPersistenceManager mockDataClassPersistenceManager2 = new MockStringPersistenceManager();
        cacheManager.addPersister( mockDataClassPersistenceManager2 );

        ObjectPersister< ? > actual = cacheManager.getObjectPersister( String.class );
        assertEquals( mockStringPersistenceManager, actual );
    }

    public void testUnRegisterDataClassPersistenceManager() {
        // register a data class persistence manager first
        MockStringPersistenceManager mockStringPersistenceManager = new MockStringPersistenceManager();
        cacheManager.addPersister( mockStringPersistenceManager );
        ObjectPersister< ? > actual = cacheManager.getObjectPersister( String.class );
        assertEquals( mockStringPersistenceManager, actual );

        // unregister it
        cacheManager.removePersister( mockStringPersistenceManager );

        // no persistence manager should be found any more
        try {
            cacheManager.getObjectPersister( String.class );
            fail( "No data class persistence manager should have been found as none had been registered" );
        } catch ( Exception ex ) {
            assertTrue( true );
        }
    }

    public void testRegister2DataClassPersistenceManagerAndRemoveOne() {
        // register 2 data class persistence manager first
        MockStringPersistenceManager mockStringPersistenceManager = new MockStringPersistenceManager();
        cacheManager.addPersister( mockStringPersistenceManager );
        MockIntegerPersistenceManager mockIntegerPersistenceManager = new MockIntegerPersistenceManager();
        cacheManager.addPersister( mockIntegerPersistenceManager );
        ObjectPersister< ? > actual = cacheManager.getObjectPersister( String.class );
        assertEquals( mockStringPersistenceManager, actual );
        actual = cacheManager.getObjectPersister( Integer.class );
        assertEquals( mockIntegerPersistenceManager, actual );

        // unregister it
        cacheManager.removePersister( mockStringPersistenceManager );

        // no persistence manager should be found any more
        try {
            cacheManager.getObjectPersister( String.class );
            fail( "No data class persistence manager should have been found as none had been registered" );
        } catch ( Exception ex ) {
            assertTrue( true );
        }

        actual = cacheManager.getObjectPersister( Integer.class );
        assertEquals( mockIntegerPersistenceManager, actual );
    }

    private class MockStringPersistenceManager extends ObjectPersister< String > {

        public MockStringPersistenceManager() {
            super( null, String.class );
        }

        @Override
        public boolean canHandleClass( Class< ? > arg0 ) {
            return arg0.equals( String.class );
        }

        @Override
        public String loadDataFromCache( Object arg0, long arg1 ) throws CacheLoadingException {
            return TEST_PERSISTED_STRING;
        }

        @Override
        public String saveDataToCacheAndReturnData( String arg0, Object arg1 ) throws CacheSavingException {
            return TEST_PERSISTED_STRING;
        }

        @Override
        public void removeAllDataFromCache() {
        }

        @Override
        public boolean removeDataFromCache( Object arg0 ) {
            return true;
        }

        @Override
        public List< String > loadAllDataFromCache() throws CacheLoadingException {
            ArrayList< String > listString = new ArrayList< String >();
            listString.add( TEST_PERSISTED_STRING );
            return listString;
        }

        @Override
        public List< Object > getAllCacheKeys() {
            return null;
        }
    }

    private class MockIntegerPersistenceManager extends ObjectPersister< Integer > {

        public MockIntegerPersistenceManager() {
            super( null, Integer.class );
        }

        @Override
        public boolean canHandleClass( Class< ? > arg0 ) {
            return arg0.equals( Integer.class );
        }

        @Override
        public Integer loadDataFromCache( Object arg0, long arg1 ) throws CacheLoadingException {
            return TEST_PERSISTED_INTEGER;
        }

        @Override
        public Integer saveDataToCacheAndReturnData( Integer arg0, Object arg1 ) throws CacheSavingException {
            return TEST_PERSISTED_INTEGER;
        }

        @Override
        public void removeAllDataFromCache() {
        }

        @Override
        public boolean removeDataFromCache( Object arg0 ) {
            return true;
        }

        @Override
        public List< Integer > loadAllDataFromCache() throws CacheLoadingException {
            ArrayList< Integer > listString = new ArrayList< Integer >();
            listString.add( TEST_PERSISTED_INTEGER );
            return listString;
        }

        @Override
        public List< Object > getAllCacheKeys() {
            return null;
        }
    }
}
