# GPS Location Tracker App

## Project Overview
This Android application displays the user's live GPS location on a map using **OpenStreetMap** and device location services. Built with Jetpack Compose and Kotlin.

**No API Key Required! No Credit Card Needed!**

## Features Implemented
✅ **Location Permission Request and Handling** - Properly requests and handles runtime location permissions  
✅ **Map Displayed on Screen** - OpenStreetMap integration (free and open-source)  
✅ **Marker Showing Current Location** - Custom marker displaying GPS coordinates  
✅ **Location Updates** - Real-time location tracking when device moves  
✅ **Basic UI (Single Screen)** - Clean, simple interface with location information card  

## Technical Implementation

### Dependencies Added
- `osmdroid-android` (6.1.18) - OpenStreetMap library for Android
- `play-services-location` (21.3.0) - Fused Location Provider

### Permissions
The app requests these permissions in `AndroidManifest.xml`:
- `ACCESS_FINE_LOCATION` - For precise GPS location
- `ACCESS_COARSE_LOCATION` - For approximate location (fallback)
- `INTERNET` - Required to download map tiles
- `ACCESS_NETWORK_STATE` - Check network connectivity
- `WRITE_EXTERNAL_STORAGE` - Cache map tiles (API 32 and below)

### Key Components

#### 1. OpenStreetMap (osmdroid)
Uses free and open-source maps with no API key required:
- Tile source: MAPNIK (default OpenStreetMap tiles)
- Multi-touch controls enabled
- Zoom level: 15
- Cached tiles for offline viewing

#### 2. FusedLocationProviderClient
Uses Google Play Services Location API for efficient battery-friendly location updates:
- Update interval: 5 seconds
- Minimum update interval: 2 seconds
- Priority: High accuracy (GPS)

#### 3. Location Updates
- Automatically starts when permission is granted
- Updates map marker position in real-time
- Camera follows current location
- Cleans up location callbacks on activity destruction

#### 4. Map Display
- OpenStreetMap with custom marker
- Marker shows current location with coordinates
- Zoom controls enabled via multi-touch
- Default location: Manila, Philippines (14.5995, 120.9842)

#### 5. UI Components
- Top card displaying GPS coordinates (latitude/longitude)
- Formatted to 6 decimal places for precision
- Loading state: "Waiting for location..."

## Setup Instructions

### 1. No API Key Required! 🎉
Unlike Google Maps, OpenStreetMap is completely free and open-source. Just sync and run!

### 2. Sync and Build
1. Open project in Android Studio
2. Sync Gradle files (File → Sync Project with Gradle Files)
3. Build and run on a physical device or emulator

### 3. Internet Connection Required
- The app needs internet to download map tiles on first use
- Tiles are cached locally for offline viewing
- Make sure your device has internet access

## Testing

### On Physical Device
1. Install the app
2. Enable internet connection (WiFi or mobile data)
3. Grant location permissions when prompted
4. Move around to see location updates
5. Marker and coordinates should update automatically

### On Emulator
1. Install the app
2. Grant location permissions
3. Use Android Studio's Location controls (Extended Controls → Location)
4. Set GPS coordinates manually or use route playback
5. Observe marker and coordinate updates

## Project Structure
```
app/src/main/java/com/example/dit3_1_jrd_act08/
├── MainActivity.kt           # Main activity with location logic
└── ui/theme/                # Theme configuration
    ├── Color.kt
    ├── Theme.kt
    └── Type.kt
```

## Location Permission Flow
1. App checks if permission is already granted
2. If not, requests permissions via ActivityResultContract
3. On grant: starts location updates immediately
4. On deny: map shows default location (Manila)

## Advantages of OpenStreetMap
✅ **No API Key Required** - No registration, no billing, no credit card  
✅ **Completely Free** - No usage limits or quotas  
✅ **Open Source** - Community-maintained map data  
✅ **Offline Caching** - Map tiles cached locally  
✅ **Privacy Friendly** - No tracking or analytics  

## Notes
- Minimum SDK: Android 7.0 (API 25)
- Target SDK: Android 14 (API 36)
- Requires Google Play Services on device (for location only)
- Location updates stop when app is destroyed to save battery
- Map tiles are downloaded from OpenStreetMap servers
- Respects OpenStreetMap's usage policy

## Future Enhancements (Optional)
- Background location tracking
- Location history trail on map
- Distance traveled calculation
- Speed and bearing display
- Save favorite locations
- Completely offline maps (download tiles)
- Different map styles (dark mode, satellite, etc.)

## Troubleshooting

**Map tiles not loading:**
- Check internet connection
- Verify INTERNET permission is granted
- Map tiles may load slowly on first launch
- Check if OpenStreetMap servers are accessible

**Blank/gray map:**
- Ensure device has internet connection
- Clear app cache and restart
- Check if your network blocks OpenStreetMap

**Location not updating:**
- Confirm location permissions are granted
- Check if device location services are enabled
- Test on physical device (emulators may have issues)
- Verify GPS signal strength (go outdoors if possible)

**Build errors:**
- Run Gradle sync
- Clean and rebuild project (Build → Clean Project → Rebuild)
- Check internet connection for dependency downloads
- Invalidate caches (File → Invalidate Caches / Restart)

## OpenStreetMap Attribution
This app uses OpenStreetMap data. OpenStreetMap® is open data, licensed under the Open Data Commons Open Database License (ODbL) by the OpenStreetMap Foundation (OSMF).
