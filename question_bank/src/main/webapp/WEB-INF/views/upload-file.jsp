<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Upload word file</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">
		<div class="row m-5">
			<div class="col-12">
				
				<form action="upload-file" class="horizontol-form" enctype="multipart/form-data" method="POST">
					<div class="card">
						<div class="card-header">
							<p class="card-title">Upload Question Word File</p>
						</div>
						<div class="card-body">
							<!-- <div class="mb-3 row">
								<label for="staticEmail" class="col-sm-2 col-form-label">Email</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										id="staticEmail" name="staticEmail" value="">
								</div>
							</div>
							<div class="mb-3 row">
								<label for="mobileNo" class="col-sm-2 col-form-label">Mobile</label>
								<div class="col-sm-10">
									<input type="number" class="form-control" id="mobileNo" name="mobileNo">
								</div>
							</div> -->
							<div class="mb-3 row">
								<label for="formFile" class="col-sm-4 col-form-label">Select Question Word File</label>
								<div class="col-sm-8">
									<input class="form-control" type="file" name="formFile" id="formFile">
								</div>
							</div>
						</div>
						<div class="card-footer text-center">
							<button type="submit" class="btn btn-success">Upload
								File</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>