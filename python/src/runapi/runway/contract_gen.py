CONTRACT = {
    "extend-video": {
        "models": ["runway"],
        "fields_by_model": {
            "runway": {
                "output_resolution": {
                    "enum": ["720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True
                },
                "source_task_id": {
                    "required": True
                }
            }
        }
    },
    "text-to-video": {
        "models": ["runway"],
        "fields_by_model": {
            "runway": {
                "aspect_ratio": {
                    "enum": ["16:9", "9:16", "1:1", "4:3", "3:4"]
                },
                "duration_seconds": {
                    "enum": [5, 10],
                    "required": True,
                    "type": "integer"
                },
                "output_resolution": {
                    "enum": ["720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True
                }
            }
        },
        "rules": [{
            "when": {
                "first_frame_image_url": {
                    "present": False
                }
            },
            "required": ["aspect_ratio"]
        }, {
            "when": {
                "first_frame_image_url": {
                    "present": True
                }
            },
            "forbidden": ["aspect_ratio"]
        }]
    }
}
