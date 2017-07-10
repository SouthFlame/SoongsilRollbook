from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^test/', views.test, name='test'),
    url(r'^$', views.login, name='login'),
    url(r'^pro_main/(?P<pk>\d+)$', views.pro_main),
    url(r'^registerUser/$', views.registerUser, name='registerUser'),
    # url(r'^start_timecnt/(?P<pk>\d+)$',views.start_timecnt, name='start_timecnt'),
    url(r'^start_timecnt/$',views.start_timecnt, name='start_timecnt'),
    url(r'^start_attendanceCheck/$',views.start_attendanceCheck, name='start_attendanceCheck'),
    url(r'^pro_add_subject/$', views.pro_add_subject, name='pro_add_subject'),
    url(r'^stu_add_subject/$', views.stu_add_subject, name='stu_add_subject'),

    # url(r'^find_pw/$', views.login_find_pw),
    url(r'^login/$', views.login),
    ##########################################################################
    url(r'^pro_main/(?P<pk>\d+)/(?P<lec_pk>\d+)$', views.pro_lecture_main),
    url(r'^pro_main/(?P<pk>\d+)/(?P<lec_pk>\d+)/manage/$', views.pro_lecture_managestu_attendance),
    url(r'^pro_main/(?P<pk>\d+)/(?P<lec_pk>\d+)/schedule/$', views.pro_lecture_managestu_attendance_add),
    url(r'^pro_main/(?P<pk>\d+)/(?P<lec_pk>\d+)/search/$', views.pro_lecture_managestu_search),



]