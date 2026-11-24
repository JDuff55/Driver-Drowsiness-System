🚗 Connected Driver Drowsiness Detection and Prevention System

A real-time driver monitoring system developed using Python, OpenCV, Computer Vision, IoT hardware and Firebase to detect signs of driver drowsiness and provide alerts to help reduce the risk of fatigue-related driving incidents.

The system uses facial landmark detection and eye monitoring techniques to identify prolonged eye closure, triggering alerts through connected hardware and recording drowsiness events in a cloud-based database.

📌 Project Overview

Driver fatigue is a significant road safety concern. This project aims to address this problem by developing an intelligent monitoring system capable of detecting early signs of driver drowsiness in real time.

Using a camera and computer vision techniques, the system monitors the driver’s eyes, calculates the Eye Aspect Ratio (EAR), and identifies prolonged eye closure.

When drowsiness is detected, the system activates warning mechanisms and records the event through Firebase, allowing drowsiness activity to be monitored and reviewed.

The project combines computer vision, embedded hardware and cloud technology into a connected driver monitoring solution.

✨ Key Features

* Real-Time Eye Monitoring: Uses OpenCV and facial landmark detection to monitor the driver’s eyes.
* Drowsiness Detection: Calculates the Eye Aspect Ratio (EAR) to identify prolonged eye closure.
* Automated Alerts: Activates connected warning components, including LEDs and an audible alarm.
* IoT Integration: Connects Raspberry Pi hardware with cloud-based services.
* Firebase Integration: Records drowsiness events and timestamps for cloud storage and monitoring.
* Event Monitoring: Supports the recording and analysis of drowsiness events and journey-related activity.

🛠️ Technologies & Tools

Technology	Purpose
Python	Core application and detection logic
OpenCV	Real-time image processing and camera input
dlib	Facial landmark detection
NumPy	Numerical calculations and EAR processing
Raspberry Pi 5	Main hardware platform
Firebase	Cloud database and event storage
USB Camera	Real-time facial monitoring
LEDs	Visual warning indicators
Bluetooth Speaker	Audible warning alerts

⚙️ How It Works

The system operates through the following stages:

1. Camera Input

A USB camera captures a continuous video feed of the driver.

2. Facial Detection

OpenCV processes the video frames, while dlib’s 68-point facial landmark model identifies facial features, including the eyes.

3. Eye Aspect Ratio Calculation

The system calculates the Eye Aspect Ratio (EAR) using facial landmarks around both eyes.

The EAR is used to determine whether the driver’s eyes are open or closed.

4. Drowsiness Detection

When the EAR falls below a predefined threshold for a consecutive number of frames, the system identifies a potential drowsiness event.

5. Alert Activation

Warning mechanisms are activated through connected hardware, using visual and audible alerts to notify the driver.

6. Cloud Data Storage

Detected events are recorded in Firebase with timestamps, supporting event tracking and dashboard monitoring.

🧠 Computer Vision Implementation

The core of the project uses the Eye Aspect Ratio (EAR) algorithm to detect eye closure.

The EAR is calculated using the distances between specific facial landmarks around the eye.

The formula is:

EAR = (||p2 - p6|| + ||p3 - p5||) / (2 × ||p1 - p4||)

Where:

* p1 and p4 represent the horizontal eye landmarks.
* p2, p3, p5 and p6 represent the vertical eye landmarks.

The system uses the following detection parameters:

Parameter	Value
Facial landmark model	68-point dlib model
Left eye landmarks	36–41
Right eye landmarks	42–47
EAR threshold	0.25
Consecutive detection frames	15
Detection approach	Real-time eye monitoring

These parameters are used to identify prolonged eye closure rather than treating every individual blink as a drowsiness event.

☁️ Firebase Integration

Firebase provides cloud storage for detected drowsiness events.

The system records event information using a structured database path:

Driver-Drowsiness-System/drowsiness_events

Recorded information includes:

* Event timestamps
* Drowsiness event details
* Journey-related monitoring data

This allows recorded events to be accessed for monitoring and further analysis.

🔌 Hardware Components

The hardware implementation uses a Raspberry Pi-based setup connected to:

* Raspberry Pi 5
* USB camera
* LED warning indicators
* Bluetooth speaker
* USB lighting

These components work alongside the Python application to provide a connected driver monitoring system.

🚀 Installation & Setup

Prerequisites

Before running the project, ensure you have:

* Python installed
* OpenCV
* NumPy
* dlib
* Raspberry Pi hardware (for the hardware functionality)
* A Firebase project

1. Clone the Repository

git clone https://github.com/JDuff55/Driver-Drowsiness-System.git
cd Driver-Drowsiness-System

2. Set Up the Python Environment

python -m venv cv_project

Activate the environment:

Windows:

cv_project\Scripts\activate

macOS / Linux:

source cv_project/bin/activate

3. Install Dependencies

If a requirements file is provided:

pip install -r requirements.txt

Otherwise, install the required Python libraries used by the project.

4. Configure Facial Landmark Detection

The project uses the dlib 68-point facial landmark predictor.

Ensure the shape_predictor_68_face_landmarks.dat model file is available and that the application is configured to reference its location.

5. Configure Firebase

1. Create or access a Firebase project.
2. Configure the Firebase database.
3. Generate the appropriate service account credentials.
4. Configure the application to access Firebase securely.

Important: Never upload Firebase service account credentials, private keys or other sensitive configuration files to a public GitHub repository.

6. Run the Application

Run the appropriate Python entry-point script from the project directory.

Refer to the source files for the application entry point and configuration requirements.

🎯 Project Objectives

The main objectives of this project were to:

* Develop a real-time computer vision application.
* Implement facial landmark detection and eye monitoring.
* Apply image processing techniques to identify signs of drowsiness.
* Integrate Python applications with Raspberry Pi hardware.
* Implement cloud-based event storage using Firebase.
* Develop a connected monitoring solution combining software and hardware.

🔮 Potential Future Improvements

Possible extensions to the project include:

* Integrating head-pose estimation for more comprehensive drowsiness detection.
* Improving detection accuracy across different lighting conditions.
* Developing more advanced journey analytics.
* Expanding mobile dashboard functionality.
* Introducing additional driver monitoring indicators.
* Evaluating detection performance across a wider range of users and driving conditions.

⚠️ Limitations

This project was developed as an academic software and hardware prototype. It is not a certified automotive safety system and should not be relied upon as a replacement for driver awareness or established vehicle safety technologies.

Detection accuracy can be affected by lighting, camera positioning, facial visibility and individual differences.

👨‍💻 Author

Jack Duff

BSc (Hons) Computer Science

Atlantic Technological University

GitHub: @JDuff55

⸻

Developed as a final-year Computer Science project, combining computer vision, Python development, IoT hardware and cloud integration.
