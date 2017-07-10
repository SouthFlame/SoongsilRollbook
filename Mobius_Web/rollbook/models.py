from django.db import models
from django.utils import timezone

class Student(models.Model):
    st_id = models.PositiveSmallIntegerField(null=False, primary_key = True)
    st_pwd = models.CharField(max_length = 15, null = False)
    st_name = models.CharField(max_length = 30, null = False)
    st_isPro = models.BooleanField(default = False)
    st_ipAddr = models.CharField(max_length=30)
    def __str__(self):
        return self.st_name + ' ' + str(self.st_id)

class Professor(models.Model):
    pro_id = models.PositiveSmallIntegerField(null=False, primary_key=True)
    pro_pwd = models.CharField(max_length=15, null=False)
    pro_name = models.CharField(max_length=30, null=False)
    pro_isPro = models.BooleanField(default=True)
    pro_Beacon = models.CharField(max_length=40, null=False)
    pro_limTime = models.PositiveSmallIntegerField(null=False, default= 180)
    pro_startTime = models.PositiveSmallIntegerField(null=False, default= 180)
    pro_ipAddr = models.CharField(max_length=30)
    def __str__(self):
        return self.pro_name

class Lecture(models.Model):
    lec_id  = models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)
    lec_professor = models.ForeignKey(Professor)
    lec_name = models.CharField(max_length=50, null=False)
    lec_access = models.CharField(max_length=10, null=False)
    lec_room = models.CharField(max_length=50, null = True)
    lec_time = models.CharField(max_length=50, null = True)
    lec_motion = models.CharField(max_length=50, null=True)
    # 강의실, 요일, 강의시간
    # lec_location = models.CharField(max_length=50, null = True)
    def __str__(self):
        return self.lec_name

class StuInLec(models.Model):
    sil_id  = models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)
    sil_lec = models.ForeignKey(Lecture)
    sil_stu = models.ForeignKey(Student)
    def __str__(self):
        return self.sil_stu.__str__() + ' ' + self.sil_lec.__str__()

class AttendanceRecord(models.Model):
    ad_id  = models.AutoField(verbose_name='ID', auto_created=True, primary_key=True, serialize=False)
    ad_date = models.DateField(auto_now_add=True)
    ad_state = models.CharField(max_length=2, default='N')
    ad_lec = models.ForeignKey(Lecture)
    ad_stu = models.ForeignKey(Student)

    def __str__(self):
        return self.ad_stu.__str__() + '의 ' + self.ad_lec.__str__() + '의 ' \
               + self.ad_date.__str__() + '상태 : ' + self.ad_state

  #  def __str__(self):
  #      return self.title

