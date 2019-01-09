/**
 * BlogInDetailCtrl
 */
app.controller('BlogInDetailCtrl',function($scope,BlogService,$routeParams,$location,$sce){
	$scope.isRejected=false
	var blogId=$routeParams.blogId
	if($routeParams.blogId!=undefined){
		
		BlogService.getBlog(blogId).then(function(response){
			//query? select * from blogpost where blogpostid=?
			$scope.blogPost=response.data  //blogapprovalform.html or blogdetails.html
			$scope.htmlContent=$sce.trustAsHtml($scope.blogPost.blogContent)
		},function(response){
			if(response.status==401)
				$location.path('/login')
		})
		
		BlogService.hasUserLikedBlogPost(blogId).then(
		function(response){
			//response.data is either 1 blogpostlikes object or empty value
			if(response.data=='')//the blogpost is not yet liked by the user
				$scope.isLiked=false
				else
					$scope.isLiked=true
		},function(response){
			if(response.status==401)
				$location.path('/login')
		})
	}
	
	
	
	
	$scope.approveBlogPost=function(blogPostId){
		BlogService.approveBlogPost(blogPostId).then(function(response){
			//redirect to getallblogswaitingforapproval
			$location.path('/blogswaitingforapproval/0')
		},function(response){
			$scope.error=response.data
			if(response.status==401)
				$location.path('/login')
		})
	}
	
	$scope.rejectBlogPost=function(blogPostId,rejectionReason){
		BlogService.rejectBlogPost(blogPostId,rejectionReason).then(function(response){
			$location.path('/blogswaitingforapproval/0')
		},function(response){
			$scope.error=response.data
			if(response.status==401)
				$location.path('/login')
		})
	}
	
	$scope.showRejectionTxt=function(){
	  $scope.isRejected=!$scope.isRejected	
	}
	
   $scope.updateLikes=function(blogPostId){
	   BlogService.updateLikes(blogPostId).then(
	    function(response){
	    	$scope.blogPost=response.data
	    	$scope.isLiked=!$scope.isLiked
	    },function(response){
	    	if(response.status==401)
				$location.path('/login')
	   })
   }	
	
   $scope.addBlogComment=function(commentTxt,blogPost){
	   var blogComment={}
	   blogComment.commentTxt=commentTxt
	   blogComment.blogPost=blogPost
	   console.log(blogComment)
	   BlogService.addBlogComment(blogComment).then(function(response){
		   $scope.blogComment=response.data
		   $scope.commentTxt=""
	   },function(response){
		   if(response.status==401)
				$location.path('/login')
	   })
   }
	
   $scope.getBlogComments=function(blogPostId){
	   BlogService.getBlogComments(blogPostId).then(
	   function(response){
		   $scope.comments=response.data //it is List<BlogComment> 
	   },function(response){
		   if(response.status==401)
				$location.path('/login')
	   })
   }
})


