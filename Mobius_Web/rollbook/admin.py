from django.contrib import admin
from .models import *

admin.site.register(Student)
admin.site.register(Lecture)
admin.site.register(Professor)
admin.site.register(StuInLec)
admin.site.register(AttendanceRecord)
